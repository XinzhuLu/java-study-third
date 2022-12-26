package space.xinzhu.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/26
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Slf4j
public class AdvancedListener {
    /**
     * 普通队列
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "exchange1", durable = "true", autoDelete = "false", type = ExchangeTypes.DIRECT),
            value = @Queue(name = "queue1", durable = "true"),
            key = "q1"
    ))
    public void listenQueue1(String msg) {
        log.debug("消费者queue1接收到消息：" + msg);
    }

    /**
     * 错误队列，死信队列
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "exchange2", type = ExchangeTypes.DIRECT),
            value = @Queue(name = "queue2", durable = "true"),
            key = "error"
    ))
    public void listenQueue2(String msg) {
        log.debug("消费者queue2接收到消息：" + msg);
    }

    /**
     * 延迟队列
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "exchange3", type = ExchangeTypes.DIRECT),
            value = @Queue(name = "queue3", durable = "true", arguments =
                    {
                            //x-message-ttl：设置队列的超时时间，10秒
                            @Argument(name = "x-message-ttl", value = "10000", type = "java.lang.Integer"),
                            //x-dead-letter-exchange：指定死信交换机
                            @Argument(name = "x-dead-letter-exchange", value = "exchange2"),
                            //x-dead-letter-routing-key：指定死信交换机路由的key
                            @Argument(name = "x-dead-letter-routing-key", value = "error")
                    }
            ),
            key = "ttl"
    ))
    public void listenQueue3(String msg) {
        log.info("消费者接收到queue3的消息：" + msg);
    }

    /**
     * DelayExchange，延迟队列
     * 配合插件DelayExchange
     * https://blog.rabbitmq.com/posts/2015/04/scheduling-messages-with-rabbitmq
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "exchange4", delayed = "true", type = ExchangeTypes.DIRECT),
            value = @Queue(name = "queue4", durable = "true"),
            key = "delay"
    ))
    public void listenQueue4(String msg) {
        log.info("消费者接收到queue4的消息：" + msg);
    }

    /**
     * 惰性队列
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(name = "exchange5", type = ExchangeTypes.DIRECT),
            value = @Queue(name = "queue5", durable = "true", arguments = @Argument(name = "x-queue-mode", value = "lazy")),
            key = "lazy"
    ))
    public void listenQueue5(String msg) {
        log.debug("消费者queue5接收到消息：" + msg);
    }
}
