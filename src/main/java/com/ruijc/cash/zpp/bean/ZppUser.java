package com.ruijc.cash.zpp.bean;

import com.ruijc.cash.bean.User;

public class ZppUser extends User {

    private String type;

    public ZppUser() {
        type = "ALIPAY";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
