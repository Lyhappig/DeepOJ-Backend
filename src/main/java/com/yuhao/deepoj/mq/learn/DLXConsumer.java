package com.yuhao.deepoj.mq.learn;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DLXConsumer {

    private static final String DEAD_EXCHANGE_NAME = "dlx-direct-exchange";

    private static final String DEAD_EXCHANGE_RK1 = "dlx_key1";

    private static final String DEAD_EXCHANGE_RK2 = "dlx_key2";

    private static final String WORK_EXCHANGE_NAME = "direct-exchange";


    private static void createDlxQueue(Channel channel) throws IOException {
        // 声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 创建队列，随机分配一个队列名称
        String queueName = "dlx_queue1";
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, DEAD_EXCHANGE_NAME, DEAD_EXCHANGE_RK1);

        String queueName2 = "dlx_queue2";
        channel.queueDeclare(queueName2, true, false, false, null);
        channel.queueBind(queueName2, DEAD_EXCHANGE_NAME, DEAD_EXCHANGE_RK2);

        DeliverCallback DeliverCallback1 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            // 拒绝消息
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [dlx_queue1] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        DeliverCallback DeliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            // 拒绝消息
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [dlx_queue2] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        channel.basicConsume(queueName, false, DeliverCallback1, consumerTag -> {
        });
        channel.basicConsume(queueName2, false, DeliverCallback2, consumerTag -> {
        });
    }

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 创建死信交换机和死信队列
        createDlxQueue(channel);

        // 声明普通交换机为 direct 模式
        channel.exchangeDeclare(WORK_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 创建一个无法完成任务的队列1
        String queueName = "defeat_queue1";
        // 指定失败队列1对应的死信队列参数
        Map<String, Object> args1 = new HashMap<>();
        // 要绑定到哪个交换机
        args1.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 指定死信要转发到哪个死信队列
        args1.put("x-dead-letter-routing-key", DEAD_EXCHANGE_RK1);
        channel.queueDeclare(queueName, true, false, false, args1);
        channel.queueBind(queueName, WORK_EXCHANGE_NAME, "key1");

        // 创建一个无法完成任务的队列2
        String queueName2 = "defeat_queue2";
        // 指定失败队列2对应的死信队列参数
        Map<String, Object> args2 = new HashMap<>();
        args2.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        args2.put("x-dead-letter-routing-key", DEAD_EXCHANGE_RK2);
        channel.queueDeclare(queueName2, true, false, false, args2);
        channel.queueBind(queueName2, WORK_EXCHANGE_NAME, "key2");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback1 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            // 拒绝消息
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [defeat_queue1] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        DeliverCallback deliverCallback2 = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            // 拒绝消息
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [defeat_queue2] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        channel.basicConsume(queueName, false, deliverCallback1, consumerTag -> {
        });
        channel.basicConsume(queueName2, false, deliverCallback2, consumerTag -> {
        });
    }
}

