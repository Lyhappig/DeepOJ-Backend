package com.yuhao.deepoj.mq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yuhao.deepoj.common.ErrorCode;
import com.yuhao.deepoj.constant.MqConstant;
import com.yuhao.deepoj.exception.BusinessException;
import com.yuhao.deepoj.model.enums.SubmissionStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class InitRabbitMq {
    public static void doInit() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            // 定义交换机
            channel.exchangeDeclare(MqConstant.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            // 待判题队列
            channel.queueDeclare(MqConstant.PENDING_QUEUE, true, false, false, null);
            channel.queueBind(MqConstant.PENDING_QUEUE, MqConstant.EXCHANGE_NAME, MqConstant.PENDING_KEY);
            // 判题成功队列
            channel.queueDeclare(MqConstant.SUCCEED_QUEUE, true, false, false, null);
            channel.queueBind(MqConstant.SUCCEED_QUEUE, MqConstant.EXCHANGE_NAME, MqConstant.SUCCEED_KEY);
            log.info("消息队列启动成功");
        } catch (IOException | TimeoutException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息队列启动失败");
        }
    }
}
