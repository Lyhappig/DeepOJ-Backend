package com.yuhao.deepoj.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicConsumer {
    private static final String EXCHANGE_NAME = "topic-exchange";

    private static final String[] bindingKeys = {"#", "*.java", "cpp.#"};

    public static void main(String[] argv) {
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();
                    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

                    String queueName = "Exchange_Worker #" + finalI;

                    // 设置队列参数：是否持久化；是否不允许非创建者使用；是否当所有消费者断开链接删除队列
                    channel.queueDeclare(queueName, true, false, false, null);
                    channel.queueBind(queueName, EXCHANGE_NAME, bindingKeys[finalI]);

                    System.out.println(" [" + Thread.currentThread().getName() + "] Waiting for " +
                            bindingKeys[finalI] + ": messages. To exit press CTRL+C");

                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        System.out.println(" [" + Thread.currentThread().getName() + "] Received '" +
                                delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                    };

                    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                    });
                } catch (TimeoutException | IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
