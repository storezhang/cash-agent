package com.ruijc.cash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CashAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CashAgentApplication.class, args);
    }
}
