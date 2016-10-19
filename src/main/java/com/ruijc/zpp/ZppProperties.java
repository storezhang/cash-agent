package com.ruijc.zpp;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * 赚泡泡配置
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "zpp")
public class ZppProperties {

    private String username;
    private String password;
    private double minCash;
    private int retry;

    @PostConstruct
    public void init() {
        retry = 10;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getMinCash() {
        return minCash;
    }

    public void setMinCash(double minCash) {
        this.minCash = minCash;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
