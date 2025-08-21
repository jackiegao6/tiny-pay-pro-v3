package com.gzc.test;

import com.gzc.infrastructure.mq.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MQTest {

    @Resource
    private EventPublisher eventPublisher;

    @Value("${spring.rabbitmq.config.producer.topic_team_success.routing_key}")
    private String routingKey;

    @Test
    public void test_rabbitmq() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        eventPublisher.publish(routingKey, "订单结算: ORDER-20250821");
        eventPublisher.publish(routingKey, "订单结算: ORDER-20250822");
        eventPublisher.publish(routingKey, "订单结算: ORDER-20250823");
        eventPublisher.publish(routingKey, "订单结算: ORDER-20250824");
        eventPublisher.publish(routingKey, "订单结算: ORDER-20250825");

        countDownLatch.await();
    }
}
