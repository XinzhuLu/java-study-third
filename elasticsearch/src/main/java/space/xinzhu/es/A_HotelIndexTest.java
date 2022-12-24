package space.xinzhu.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/24
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
public class A_HotelIndexTest {

    private RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(HttpHost.create("http://192.168.100.150:9200")));

    //创建hotel索引库
    @Test
    public void testCreateIndex() throws IOException {

    }
}
