package com.ruijc.zpp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ZppAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZppAgentApplication.class, args);
    }
}
