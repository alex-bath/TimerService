package com.ticketclever.go.timerservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories // Enable database integration
@EnableCaching // Enable annotation based caching
@EnableFeignClients // Enable declarative REST API support
@EnableCircuitBreaker // Use Netflix's Hystrix library
@EnableDiscoveryClient
@EntityScan(basePackageClasses = {TimerServiceApplication.class, Jsr310JpaConverters.class})
public class TimerServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TimerServiceApplication.class, args);
    }

}