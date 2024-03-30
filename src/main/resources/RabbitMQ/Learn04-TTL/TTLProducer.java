package com.yuhao.deepoj.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class TTLProducer {
    private final static String QUEUE_NAME = "ttl_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 为指定队列中的所有消息设置过期时间
            Map<String, Object> strategy = new HashMap<>();
            strategy.put("x-message-ttl", 60000);
            channel.queueDeclare(QUEUE_NAME, false, false, false, strategy);
            // 为指定消息设置过期时间
            String message = "Hello World!";
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .expiration("10000")
                    .build();
            channel.basicPublish("", QUEUE_NAME, properties, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
