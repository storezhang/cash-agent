package com.ruijc.zpp;

import com.ruijc.http.HttpClient;
import com.ruijc.util.MapUtils;
import com.ruijc.util.NumberUtils;
import com.ruijc.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class ZppAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZppAgentApplication.class, args);
    }
}
