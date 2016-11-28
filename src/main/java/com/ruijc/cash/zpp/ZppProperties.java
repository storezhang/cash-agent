package com.ruijc.cash.zpp;

import com.ruijc.cash.zpp.bean.ZppUser;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 赚泡泡配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "cash.zpp")
public class ZppProperties {

    public static final String LOG_STORE = "zpp";
    public static final String LOG_TOP_CASH = "cash";
    public static final String LOG_TOP_LOGIN = "login";

    private List<ZppUser> users;
    private double minCash;

    public ZppProperties() {
        minCash = 5;
    }

    public List<ZppUser> getUsers() {
        return users;
    }

    public void setUsers(List<ZppUser> users) {
        this.users = users;
    }

    public double getMinCash() {
        return minCash;
    }

    public void setMinCash(double minCash) {
        this.minCash = minCash;
    }
}
