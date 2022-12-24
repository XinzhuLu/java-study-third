package space.xinzhu.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import space.xinzhu.es.constants.HotelConstants;

import java.io.IOException;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/24
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
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
}
