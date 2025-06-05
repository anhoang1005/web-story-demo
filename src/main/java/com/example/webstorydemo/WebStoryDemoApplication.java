package com.example.webstorydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class WebStoryDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebStoryDemoApplication.class, args);
    }

}
