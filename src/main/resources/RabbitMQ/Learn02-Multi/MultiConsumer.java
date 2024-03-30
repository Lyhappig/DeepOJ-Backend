package com.yuhao.deepoj.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class MultiConsumer {
    private static final String TASK_QUEUE_NAME = "multi_queue";

    private static final ThreadLocal<Integer> threadLocalVar = new ThreadLocal<>();

    public static void main(String[] argv) {
        for (int i = 0; i < 2; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                threadLocalVar.set(finalI);
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("localhost");
                    final Connection connection = factory.newConnection();
                    final Channel channel = connection.createChannel();

                    channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
                    System.out.println(" [" + Thread.currentThread().getName() + "] Waiting for messages. To exit press CTRL+C");

                    // 设置每个 Worker 最多处理的任务个数
                    // MQ 会让每个 Worker 不会同时处理超过 n 个任务
                    // 类似于给每个 Worker 维护一个目前待办任务的队列，该队列长度最大为 n，任务是均匀分配的
                    channel.basicQos(1);

                    int temp = threadLocalVar.get();

                    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        System.out.println(" [" + Thread.currentThread().getName() + "] Received '" + message + "'");
                        try {
                            // 执行任务
                            if (temp == 0) {
                                Thread.sleep(20000);
                            } else {
                                Thread.sleep(2000);
                            }
                            // 完成任务，确认机制
                            System.out.println("[" + Thread.currentThread().getName() + "] Done");
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        } catch (InterruptedException ignored) {
                            // 未完成任务时，默认将消息重新入队
                            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
                        }
                    };
                    channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, consumerTag -> {});
                } catch (TimeoutException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.setName("Thread #" + i);
            thread.start();
        }
        threadLocalVar.remove();
    }
}
