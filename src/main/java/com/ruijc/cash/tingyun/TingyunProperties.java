package com.ruijc.cash.tingyun;

import com.ruijc.cash.bean.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 蚂蚁网页挂机配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "cash.tingyun")
public class TingyunProperties {

    public static final String LOG_STORE = "tingyun";
    public static final String LOG_TOP_CASH = "cash";
    public static final String LOG_TOP_LOGIN = "login";

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
