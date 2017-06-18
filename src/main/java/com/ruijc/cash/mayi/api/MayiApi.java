package com.ruijc.cash.mayi.api;

import com.ruijc.http.HttpClient;
import com.ruijc.util.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 蚂蚁网页挂机工具类
 *
 * @author Storezhang
 */
@Service
public class MayiApi {

    @Autowired
    private HttpClient client;

    public boolean login(String username, String password) {
        boolean success = false;

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        String ret = client.post("http://www.mayiluntan.com/login/dologin.html", params, null, "http://www.mayiluntan.com", "", null, "UTF-8");
        if (!ret.contains("登录成功")) {
            success = false;
        } else {
            success = true;
        }

        return success;
    }

    public double getMoney() {
        double money = 0;

        String cashInfo = client.get("http://www.mayiluntan.com/home/tixian/index.html");
        Document doc = Jsoup.parse(cashInfo);
        Elements cashElement = doc.select("#total_xianjin_count");
        if (null == cashElement) {
            return money;
        }

        money = NumberUtils.getDouble(cashElement.text());

        return money;
    }

    public int getSurplusTixianTimes() {
        int surplus = 0;

        String surplusInfo = client.get("http://www.mayiluntan.com/home/tixian/index.html");
        Document doc = Jsoup.parse(surplusInfo);
        Element timesElement = doc.select(".guaji_index h2 span").last();
        if (null == timesElement) {
            return surplus;
        }

        surplus = NumberUtils.getInt(timesElement.text());

        return surplus;
    }

    public void logout() {
        client.clearCookies();
    }

    public boolean cash(int money) {
        boolean success;

        Map<String, String> cashParams = new HashMap<String, String>();
        cashParams.put("tixian_jine", money + "");
        String cashRet = client.post("http://www.mayiluntan.com/home/tixian/create.html", cashParams);
        if (!cashRet.contains("提现成功")) {
            success = false;
            return success;
        }

        success = true;

        return success;
    }
}
