package com.yuhao.deepoj.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProducerConfirmCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            log.error("Error: Publisher failed to send message[{}] to Rabbitmq broker, {}", correlationData.getId(), cause);
        } else {
            log.info("Publisher successfully send message to Rabbitmq broker");
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("Error {}: {}", returnedMessage.getReplyCode(), returnedMessage.getReplyText());
        log.error("Rabbitmq broker failed to send message[{}] to target-queue by exchange[{}] through routing-key[{}]", returnedMessage.getMessage(), returnedMessage.getExchange(), returnedMessage.getRoutingKey());
    }
}
