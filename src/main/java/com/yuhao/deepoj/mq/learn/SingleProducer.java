package com.yuhao.deepoj.mq.learn;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class SingleProducer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // 建立链接，创建频道（可以理解为客户端）
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 创建消息队列或者复用已经创建的消息队列
            // 设置队列参数：是否持久化；是否不允许非创建者使用；是否当所有消费者断开链接删除队列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
