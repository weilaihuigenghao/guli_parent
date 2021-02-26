package com.bdqn.eduorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.bdqn"})
@MapperScan("com.bdqn.eduorder.mapper")
public class OrderApplition {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplition.class, args);
    }
}

