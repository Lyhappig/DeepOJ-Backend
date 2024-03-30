package com.yuhao.deepoj.mq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageProducerTest {

    @Resource
    MessageProducer producer;

    @Test
    void sendMessage() {
        producer.sendMessage("code_exchange", "judge", "hello");
    }
}