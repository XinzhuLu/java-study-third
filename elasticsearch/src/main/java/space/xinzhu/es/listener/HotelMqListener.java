package space.xinzhu.es.listener;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.xinzhu.es.constants.HotelMQConstants;
import space.xinzhu.es.service.IHotelService;
import space.xinzhu.es.vo.Hotel;
import space.xinzhu.es.vo.HotelDoc;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/25
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Component
public class HotelMqListener {

    @Autowired
    private IHotelService hotelService;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 监听酒店新增或修改的业务
     *
     * @param id 酒店id
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = HotelMQConstants.EXCHANGE_NAME),
            value = @Queue(name = HotelMQConstants.QUEUE_INSERT_NAME),
            key = HotelMQConstants.ROUTING_KEY_INSERT
    ))
    public void listenHotelInsertOrUpdate(Long id) throws Exception {
        // 查询酒店数据，应该基于Feign远程调用hotel-admin，根据id查询酒店数据（现在直接去数据库查）
        Hotel hotel = hotelService.getById(id);
        // 转换
        HotelDoc hotelDoc = new HotelDoc(hotel);
        // 1.创建Request
        IndexRequest request = new IndexRequest("hotel").id(id.toString());
        // 2.准备参数
        request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        // 3.发送请求
        restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 监听酒店删除的业务
     *
     * @param id 酒店id
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = HotelMQConstants.EXCHANGE_NAME),
            value = @Queue(name = HotelMQConstants.QUEUE_DELETE_NAME),
            key = HotelMQConstants.ROUTING_KEY_DELETE
    ))
    public void listenHotelDelete(Long id) throws Exception {
        // 1.创建request
        DeleteRequest request = new DeleteRequest("hotel", id.toString());
        // 2.发送请求
        restHighLevelClient.delete(request, RequestOptions.DEFAULT);
    }
}