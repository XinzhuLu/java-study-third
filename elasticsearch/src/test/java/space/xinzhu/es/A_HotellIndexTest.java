package space.xinzhu.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import space.xinzhu.es.constants.HotelConstants;

import java.io.IOException;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/24
 * --------------------------------------------
 * 针对索引库的操作：
 *      CreateIndexRequest
 *      GetIndexRequest
 *      DeleteIndexRequest
 **/
@SpringBootTest
public class A_HotellIndexTest {

    private RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(HttpHost.create("http://110.41.157.223:9200")));

    //创建hotel索引库
    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("hotel")
                .source(HotelConstants.SOURCE, XContentType.JSON);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    //删除hotel索引库
    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("hotel");
        client.indices().delete(request, RequestOptions.DEFAULT);
    }

    //判断hotel索引库是否存在
    @Test
    public void testExistsIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("hotel");
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
}
