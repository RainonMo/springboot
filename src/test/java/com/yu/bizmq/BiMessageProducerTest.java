package com.yu.bizmq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class BiMessageProducerTest {
    @Resource
    private BiMessageProducer biMessageProducer;

    @Test
    void sendMessage() {
        biMessageProducer.sendMessage(BiMqConstant.BI_EXCHANGE_NAME,BiMqConstant.BI_ROUTING_KEY,"你好");
    }
}