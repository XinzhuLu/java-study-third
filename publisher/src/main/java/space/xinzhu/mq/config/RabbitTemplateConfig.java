package space.xinzhu.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @description: MQ配置类
 * Created by 馨竹 on 2022/12/26
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@Slf4j
@Configuration
public class RabbitTemplateConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 1. 从spring容器中取出RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        //2. 设置回调方法ConfirmCallBack
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error("消息未发送到交换机! {}", cause);
            } else {
                log.info("消息已发送到交换机!");
            }
        });
        //3. 设置回调方法ReturnCallBack
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息未发送到队列，replyCode:{}, replyText:{}, routingKey:{}", replyCode, replyText, routingKey);
        });
    }
}