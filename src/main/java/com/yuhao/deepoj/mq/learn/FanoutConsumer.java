package com.yuhao.deepoj.mq.learn;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class FanoutConsumer {
    private static final String EXCHANGE_NAME = "fanout-exchange";

    public static void main(String[] argv) {
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    Connection connection = factory.newConnection();
                    Channel channel = connection.createChannel();
                    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

                    String queueName = "Exchange_Worker #" + finalI;

                    channel.queueDeclare(queueName, true, false, false, null);
                    channel.queueBind(queueName, EXCHANGE_NAME, "");
                    System.out.println(" [" + Thread.currentThread().getName() + "] Waiting for messages. To exit press CTRL+C");

                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        System.out.println(" [" + Thread.currentThread().getName() + "] Received '" + message + "'");
                    };

                    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
                } catch (TimeoutException | IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}
