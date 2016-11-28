package com.ruijc.cash.mayi;

import com.ruijc.cash.bean.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 蚂蚁网页挂机配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "cash.mayi")
public class MayiProperties {

    public static final String LOG_STORE = "mayi";
    public static final String LOG_TOP_CASH = "cash";
    public static final String LOG_TOP_LOGIN = "login";

    private List<User> users;
    private double minCash;

    public MayiProperties() {
        minCash = 1;
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
}
