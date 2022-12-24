package space.xinzhu.es;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import space.xinzhu.es.service.IHotelService;
import space.xinzhu.es.vo.Hotel;
import space.xinzhu.es.vo.HotelDoc;

import java.io.IOException;
import java.util.List;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/24
 * --------------------------------------------
 * 针对文档的操作：
 *      IndexRequest
 *      GetRequest
 *      DeleteRequest
 *      UpdateRequest
 *      BulkRequest
 **/
@SpringBootTest
@Slf4j
public class B_HotelDocumentTest {

    private RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(HttpHost.create("http://110.41.157.223:9200")));

    @Autowired
    private IHotelService hotelService;

    //添加文档
    @Test
    void testAddDocument() throws IOException {
        //1. 查询mysql数据库
        Hotel hotel = hotelService.getById(36934L);
        log.info(hotel.toString());
        //2. 将Hotel转为HotelDoc
        HotelDoc hotelDoc = new HotelDoc(hotel);
        //3. 将HotelDoc转为json字符串
        String jsonString = JSON.toJSONString(hotelDoc);
        //4. 创建IndexRequest对象，添加json文档数据，指定id
        IndexRequest request = new IndexRequest("hotel")
                .id(hotelDoc.getId().toString())
                .source(jsonString, XContentType.JSON);
        //5. 发送请求，添加文档数据
        client.index(request, RequestOptions.DEFAULT);
    }

    //根据id获取文档数据，反序列到HotelDoc对象中
    @Test
    void testGetDocumentById() throws IOException {
        GetRequest request = new GetRequest("hotel").id("61083");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        HotelDoc hotelDoc = JSON.parseObject(response.getSourceAsString(), HotelDoc.class);
        System.out.println(hotelDoc);
    }

    //根据id删除文档
    @Test
    void testDeleteDocumentById() throws IOException {
        DeleteRequest request = new DeleteRequest("hotel").id("36934");
        client.delete(request, RequestOptions.DEFAULT);
    }

    //根据id更新文档数据，设置price=870
    @Test
    void testUpdateById() throws IOException {
        UpdateRequest request = new UpdateRequest("hotel", "61083")
                .doc("price", 870, "name", "Rose Style");
        client.update(request, RequestOptions.DEFAULT);
    }

    //批量添加文档，将mysql中的hotel数据同步到es中
    @Test
    void testBulkRequest() throws IOException {
        List<Hotel> list = hotelService.list();
        BulkRequest request = new BulkRequest();
        for (Hotel hotel : list) {
            HotelDoc hotelDoc = new HotelDoc(hotel);
            String json = JSON.toJSONString(hotelDoc);
            request.add(new IndexRequest("hotel")
                    .id(hotel.getId().toString())
                    .source(json, XContentType.JSON));
        }
        client.bulk(request, RequestOptions.DEFAULT);
    }
}
