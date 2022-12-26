package space.xinzhu.mq;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/26
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Slf4j
@SpringBootTest
public class ConfirmTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testMessage() throws InterruptedException {
        //1. 创建CorrelationData对象，设置回调方法ConfirmCallback
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //2. 发送消息
        rabbitTemplate.convertAndSend("amq.direct", "q1", "hello...", correlationData);
        //3. 休眠一会儿，等待successCallBack回调，不然程序结束看不到效果
        Thread.sleep(2000);
    }

    @Test
    public void testDurableMessage() {

        Message message = MessageBuilder
                .withBody("hello, Spring...".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT) // 消息持久化
                .build();
        rabbitTemplate.convertAndSend("exchange1", "q1", message );
        log.info("消息已经成功发送！");
    }

    @Test
    public void testTTLMessage() {
        rabbitTemplate.convertAndSend("exchange3", "ttl", "hello, ttl message...");
        log.info("消息已经成功发送！");
    }

    //指定ttl
    @Test
    public void testTTLMessage2() {
        // 创建消息
        Message message = MessageBuilder
                .withBody("hello, ttl message......".getBytes(StandardCharsets.UTF_8))
                .setExpiration("5000")
                .build();
        rabbitTemplate.convertAndSend("exchange3", "ttl", message);
        //rabbitTemplate.convertAndSend("exchange3", "ttl", "hello, ttl message...");
        log.info("消息已经成功发送！");
    }

    @Test
    public void testSendDelayMessage() throws InterruptedException {
        // 1.准备消息
        Message message = MessageBuilder
                .withBody("hello, ttl message......".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .setHeader("x-delay", 5000)
                .build();
        rabbitTemplate.convertAndSend("exchange4", "delay", message);
        log.info("发送消息成功");
    }

    @Test
    public void testLazyQueue() {
        long b = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            // 发送消息，queue5是惰性队列
            rabbitTemplate.convertAndSend("queue5", "hello, Spring...");
            // rabbitTemplate.convertAndSend("queue2", "hello, Spring...");
        }
        long e = System.nanoTime();
        System.out.println(e - b);
    }

}
