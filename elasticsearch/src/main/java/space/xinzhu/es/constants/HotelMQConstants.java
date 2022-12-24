package space.xinzhu.es.constants;

public class HotelMQConstants {

    //交换机名称
    public static final String EXCHANGE_NAME = "hotel.direct";
    //添加队列名称
    public static final String QUEUE_INSERT_NAME = "hotel.queue.insert";
    //删除队列名称
    public static final String QUEUE_DELETE_NAME = "hotel.queue.delete";
    //添加队列路由的key
    public static final String ROUTING_KEY_INSERT = "hotel.insert";
    //删除队列路由的key
    public static final String ROUTING_KEY_DELETE = "hotel.delete";

}
