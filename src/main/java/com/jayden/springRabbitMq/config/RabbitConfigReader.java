package com.jayden.springRabbitMq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:application.yml")
/*
 * 이 프로젝트는 app1 -> app2 로 간단한 메세지를 보내보는 예제 세팅
 * 실 사용에서는 Sender 와 Receiver는 나누는게 좋을듯하다.
 */
public class RabbitConfigReader {

    @Value("${app1.exchange.name}")
    private String test1AppExchange;

    @Value("${app1.queue.name}")
    private String test1AppQueue;

    @Value("${app1.routing.key}")
    private String test1AppRoutingKey;

    @Value("${app2.exchange.name}")
    private String test2AppExchange;

    @Value("${app2.queue.name}")
    private String test2AppQueue;

    @Value("${app2.routing.key}")
    private String test2AppRoutingKey;
}
