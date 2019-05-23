package com.hyh.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ListenerApplication.class,args);
    }
}
