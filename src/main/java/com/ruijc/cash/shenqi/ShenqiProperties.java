package com.ruijc.cash.shenqi;

import com.ruijc.cash.bean.User;
import com.ruijc.cash.zpp.bean.ZppUser;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 神器配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "cash.shenqi")
public class ShenqiProperties {

    public static final String LOG_STORE = "shenqi";
    public static final String LOG_TOP_CASH = "cash";
    public static final String LOG_TOP_MSG = "msg";
    public static final String LOG_TOP_LOGIN = "login";

    private List<User> users;
    private int minCash;
    private int rate;
    private int maxMsgSize;

    public ShenqiProperties() {
        minCash = 10;
        rate = 300;
        maxMsgSize = 50;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getMinCash() {
        return minCash;
    }

    public void setMinCash(int minCash) {
        this.minCash = minCash;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getMaxMsgSize() {
        return maxMsgSize;
    }

    public void setMaxMsgSize(int maxMsgSize) {
        this.maxMsgSize = maxMsgSize;
    }
}
