package com.ruijc.zpp;

import com.ruijc.zpp.bean.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 赚泡泡配置
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "zpp")
public class ZppProperties {

    private List<User> users;
    private double minCash;
    private int retry;

    @PostConstruct
    public void init() {
        minCash = 5;
        retry = 10;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
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
