package com.yuhao.deepoj.mq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TopicProducer {

    private static final String EXCHANGE_NAME = "topic-exchange";

    private static final String message = "hello";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String routingKey = scanner.nextLine();
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [producer] Sent '" + routingKey + "':'" + message + "'");
            }
        }
    }
}
