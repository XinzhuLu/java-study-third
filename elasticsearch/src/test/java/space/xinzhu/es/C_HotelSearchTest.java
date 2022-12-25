package space.xinzhu.es;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import space.xinzhu.es.vo.HotelDoc;

import java.io.IOException;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/25
 * --------------------------------------------
 * 针对索引库的检索操作
 *      SearchRequest
 *          QueryBuilders.matchAllQuery
 *          QueryBuilders.multiMatchQuery
 *          QueryBuilders.boolQuery
 *          QueryBuilders.termQuery
 *          QueryBuilders.rangeQuery
 **/
@SpringBootTest
public class C_HotelSearchTest {

    private RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(HttpHost.create("http://110.41.157.223:9200")));

    //matchAll查询
    @Test
    void testMatchAll() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.matchAllQuery());
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //TODO 处理结果集
        long total = response.getHits().getTotalHits().value;
        System.out.println("总条数：" + total);
        for (SearchHit hit : response.getHits().getHits()) {
            HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }
    //match查询，name=上海外滩
    @Test
    void testMatch() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.matchQuery("name", "上海外滩")).query(QueryBuilders.termQuery("city" , "上海"))
                .query(QueryBuilders.rangeQuery("price").gte(100).lte(150))
                .sort("score")
                .size(20)
                .from(0);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //TODO 处理结果集
        long total = response.getHits().getTotalHits().value;
        System.out.println("总条数：" + total);
        for (SearchHit hit : response.getHits().getHits()) {
            HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //bool查询，city=上海，price<=550
    @Test
    void testBool() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source().query(
                QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("city", "上海"))
                        .must(QueryBuilders.rangeQuery("price").lte(550))
        );

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //TODO 处理结果集
        long total = response.getHits().getTotalHits().value;
        System.out.println("总条数：" + total);
        for (SearchHit hit : response.getHits().getHits()) {
            HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //分页排序，page=1，size=5，price降序
    @Test
    void testSortAndPage() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .from(0).size(5)
                .sort("price", SortOrder.DESC);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //TODO 处理结果集
        long total = response.getHits().getTotalHits().value;
        System.out.println("总条数：" + total);
        for (SearchHit hit : response.getHits().getHits()) {
            HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);
            System.out.println(hotelDoc);
        }
    }

    //高亮查询，name,brand,business=如家酒店
    @Test
    void testHighlight() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source()
                .query(QueryBuilders.matchQuery("all", "上海外滩"))
                .highlighter(new HighlightBuilder().field("name").requireFieldMatch(false));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //TODO 处理结果集
        long total = response.getHits().getTotalHits().value;
        System.out.println("总条数：" + total);
        for (SearchHit hit : response.getHits().getHits()) {
            HotelDoc hotelDoc = JSON.parseObject(hit.getSourceAsString(), HotelDoc.class);
            //处理高亮数据
            HighlightField field = hit.getHighlightFields().get("name");
            if (field != null) {
                hotelDoc.setName(field.fragments()[0].toString()); //使用高亮信息重新赋值
            }
            System.out.println(hotelDoc);
        }
    }

    //Bucket聚合查询，按brand分组，city分组
    @Test
    void testAgg() throws IOException {
        SearchRequest request = new SearchRequest("hotel");
        request.source().size(0);
        request.source()
                .aggregation(AggregationBuilders.terms("brandAgg").field("brand").size(10))
                .aggregation(AggregationBuilders.terms("cityAgg").field("city").size(10));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        //TODO 解析结果，获取品牌分桶数据
        Terms brandAgg = response.getAggregations().get("brandAgg");
        for (Terms.Bucket bucket : brandAgg.getBuckets()) {
            String brandName = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            System.out.println(brandName + ":" + docCount);
        }
        //TODO 解析结果，获取城市分桶数据
        System.out.println("====================");
        Terms cityAgg = response.getAggregations().get("cityAgg");
        for (Terms.Bucket bucket : cityAgg.getBuckets()) {
            String cityAggName = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();
            System.out.println(cityAggName + ":" + docCount);
        }
    }
}