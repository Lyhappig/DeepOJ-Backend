package com.yuhao.deepoj.mq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageProducer {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ProducerConfirmCallBack producerConfirmCallBack;

    /**
     * 发送消息
     *
     * @param exchange
     * @param routingKey
     * @param messageStr
     */
    public void sendMessage(String exchange, String routingKey, String messageStr) {
        // MQ 代理未收到消息的回调处理
        rabbitTemplate.setConfirmCallback(producerConfirmCallBack);

        // MQ 代理将消息投递到队列失败回调处理
        rabbitTemplate.setReturnsCallback(producerConfirmCallBack);

        // 构建消息体
        Message message = MessageBuilder.withBody(messageStr.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
