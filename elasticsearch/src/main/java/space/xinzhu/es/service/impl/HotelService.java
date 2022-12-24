package space.xinzhu.es.service.impl;

import space.xinzhu.es.mapper.HotelMapper;
import space.xinzhu.es.service.IHotelService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.xinzhu.es.vo.Hotel;
import space.xinzhu.es.vo.HotelDoc;
import space.xinzhu.es.vo.PageResult;
import space.xinzhu.es.vo.RequestParams;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/24
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Slf4j
@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public PageResult search(RequestParams params) throws Exception {
        //TODO 1. 创建request
        SearchRequest request = new SearchRequest("hotel");
        //TODO 2. 构造条件
        buildBasicQuery(params, request);
        //TODO 2. 设置分页
        request.source()
                .from((params.getPage() - 1) * params.getSize())
                .size(params.getSize());
        //TODO 3. 发送请求
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        //TODO 4. 解析响应
        // 4.1. 总条数
        long total = response.getHits().getTotalHits().value;
        // 4.2. 获取文档数组
        SearchHit[] hits = response.getHits().getHits();
        List<HotelDoc> collect = Arrays.stream(hits).map(hit -> {
            HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);
            // 处理高亮结果
            Map<String, HighlightField> map = hit.getHighlightFields();
            HighlightField hField = map.get("name");
            if (hField != null) {
                String hName = hField.getFragments()[0].toString();
                hotelDoc.setName(hName);//使用高亮内容替换原始的内容
            }
            // 处理排序信息
            Object[] sortValues = hit.getSortValues();
            if (sortValues.length > 0) {
                hotelDoc.setDistance(sortValues[0]);
            }

            return hotelDoc;
        }).collect(Collectors.toList());

        return new PageResult(total, collect);
    }

    /**
     * 将请求参数封装为查询条件
     *
     * @param params  请求参数
     * @param request 查询请求
     */
    private void buildBasicQuery(RequestParams params, SearchRequest request) {
        // 1.创建bool查询
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        //2. 添加各种小query到bool中
        boolQuery.must(QueryBuilders.matchAllQuery());
        //TODO 1.1.条件查询 matchQuery
        String key = params.getKey();
        if (StringUtils.isNotBlank(key)) {
            boolQuery.must(QueryBuilders.matchQuery("all", key));
        }
        //TODO 1.2.品牌查询 termQuery
        String brand = params.getBrand();
        if (StringUtils.isNotBlank(brand)) {
            boolQuery.filter(QueryBuilders.termQuery("brand", brand));
        }
        //TODO 1.3.城市查询 termQuery
        String city = params.getCity();
        if (StringUtils.isNotBlank(city)) {

            boolQuery.filter(QueryBuilders.termQuery("city", city));
        }
        //TODO 1.4.星级查询 termQuery
        String starName = params.getStarName();
        if (StringUtils.isNotBlank(starName)) {
            boolQuery.filter(QueryBuilders.termQuery("starName", starName));
        }
        //TODO 1.5.价格范围查询 rangeQuery
        Integer minPrice = params.getMinPrice();
        Integer maxPrice = params.getMaxPrice();
        if (minPrice != null && maxPrice != null) {
            boolQuery.filter(QueryBuilders.rangeQuery("price").gte(minPrice).lte(maxPrice));
        }
        //TODO 1.6.距离排序 SortBuilders
        String location = params.getLocation();
        if (StringUtils.isNotBlank(location)) {
            request.source()
                    .sort(SortBuilders
                            .geoDistanceSort("location", new GeoPoint(location))
                            .order(SortOrder.ASC)
                            .unit(DistanceUnit.KILOMETERS)
                    );
        }
        //TODO 1.7.算分函数查询 functionScoreQuery
        FunctionScoreQueryBuilder.FilterFunctionBuilder[] functionBuilders = { // function数组
                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                        QueryBuilders.termQuery("isAD", true), // 过滤条件
                        ScoreFunctionBuilders.weightFactorFunction(10) // 算分函数
                )
        };
        FunctionScoreQueryBuilder functionScoreQuery = QueryBuilders.functionScoreQuery(boolQuery, functionBuilders);
        // 3.设置查询条件
        request.source().query(functionScoreQuery);
    }


    @Override
    public Map<String, List<String>> filters(RequestParams params) throws Exception {
        //TODO 1. 创建request
        SearchRequest request = new SearchRequest("hotel");
        //TODO 2. 构造条件
        buildBasicQuery(params, request);
        request.source().size(0);
        //TODO 3. 设置聚合条件
        request.source()
                .aggregation(AggregationBuilders.terms("brand").field("brand").size(100))
                .aggregation(AggregationBuilders.terms("city").field("city").size(100))
                .aggregation(AggregationBuilders.terms("starName").field("starName").size(100));
        //TODO 4. 发送请求
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        //TODO 5. 解析响应
        //TODO 解析结果，获取品牌分桶数据
        Terms brand = response.getAggregations().get("brand");
        List<String> brandList = brand.getBuckets().stream().map(bucket -> {
            return bucket.getKeyAsString();
        }).collect(Collectors.toList());
        //TODO 解析结果，获取城市分桶数据
        Terms city = response.getAggregations().get("city");
        List<String> cityList = city.getBuckets().stream().map(bucket -> {
            return bucket.getKeyAsString();
        }).collect(Collectors.toList());
        //TODO 解析结果，获取星级分桶数据
        Terms starName = response.getAggregations().get("starName");
        List<String> starNameList = starName.getBuckets().stream().map(bucket -> {
            return bucket.getKeyAsString();
        }).collect(Collectors.toList());

        Map<String, List<String>> result = new HashMap<>(3);
        result.put("brand", brandList);
        result.put("city", cityList);
        result.put("starName", starNameList);
        return result;
    }

    @Override
    public List<String> suggestion(String key) throws Exception {
        //TODO 1. 创建request
        SearchRequest request = new SearchRequest("hotel");
        //TODO 2.设置suggest条件
        request.source().suggest(new SuggestBuilder()
                .addSuggestion("hotelSuggest",
                        SuggestBuilders
                                .completionSuggestion("suggestion")
                                .size(10)
                                .skipDuplicates(true)
                                .prefix(key)
                ));
        //TODO 3. 发送请求
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        //TODO 4. 解析响应
        Suggest suggest = response.getSuggest();
        CompletionSuggestion suggestion = suggest.getSuggestion("hotelSuggest");
        List<String> list = suggestion.getOptions().stream().map(option -> {
            return option.getText().toString();
        }).collect(Collectors.toList());

        return list;
    }
}
