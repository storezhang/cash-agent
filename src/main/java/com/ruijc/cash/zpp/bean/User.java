package com.ruijc.cash.zpp.bean;

import com.ruijc.BaseObject;

/**
 * 赚泡泡用户
 *
 * @author Storezhang
 */
public class User extends BaseObject {

    private String username;
    private String password;

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
}
