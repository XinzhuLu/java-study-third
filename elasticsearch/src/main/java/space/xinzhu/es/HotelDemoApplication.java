package space.xinzhu.es;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("space.xinzhu.es.mapper")
@SpringBootApplication
@Slf4j
public class HotelDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelDemoApplication.class, args);
        log.info("http://localhost:8089");
    }

    //初始化es客户端
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(
                HttpHost.create("http://110.41.157.223:9200")
        ));
    }
}
