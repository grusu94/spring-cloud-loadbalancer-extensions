package com.example.load.balancer.extensions.examples.jms;

import com.example.load.balancer.extensions.context.ExecutionContext;
import com.example.load.balancer.extensions.context.ExecutionContextHolder;
import com.example.load.balancer.extensions.support.EnableContextPropagation;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

import javax.inject.Inject;

@SpringBootApplication
@EnableContextPropagation
public class JmsTest implements ApplicationRunner {
    @Inject
    JmsTemplate jmsTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExecutionContextHolder.current().put("favorite-zone", "zone1");
        String expected = "message";
        jmsTemplate.convertAndSend("queue", expected);
        jmsTemplate.setReceiveTimeout(1000);
        ExecutionContextHolder.remove();
        String actual = (String) jmsTemplate.receiveAndConvert("queue");
        ExecutionContext current = ExecutionContextHolder.current();
        System.exit(expected.equals(actual) && "zone1".equals(current.get("favorite-zone")) ? 0 : 1);
    }

    public static void main(String[] args) {
        SpringApplication.run(JmsTest.class, "--spring.config.name=jms");
    }
}
