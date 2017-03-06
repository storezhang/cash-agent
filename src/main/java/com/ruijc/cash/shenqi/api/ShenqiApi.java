package com.ruijc.cash.shenqi.api;

import com.ruijc.http.HttpClient;
import com.ruijc.util.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 神器工具类
 *
 * @author Storezhang
 */
@Service
public class ShenqiApi {

    @Autowired
    private HttpClient client;


    public boolean login(String username, String password) {
        boolean success;

        if (!client.downloadFile("http://www.shen-qi.com/yz/code_char.php", "shenqi-code.jpg")) {
            success = false;
            return success;
        }
        //http://data.tehir.cn/url/Api/VCRInterface.ashx?apikey=646B7F4EB194A042E76E2615924FF84A&flag=zhuanpaopao&ImgUrl=http://www.zhuanpaopao.com/welcome/verifyCode
        String code = client.post("http://data.tehir.cn/url/Api/VCRInterface.ashx?apikey=646B7F4EB194A042E76E2615924FF84A&flag=shenqi", null, null, "", "img", new File("shenqi-code.jpg"));
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);
        params.put("code", code);

        String ret = client.post("http://www.shen-qi.com/webdo/logdo.php", params, null, "http://www.shen-qi.com", "", null);
        if (!ret.contains("index.php")) {//﻿<script language='javascript'> location='/index.php';</script>
            success = false;
        } else {
            success = true;
        }

        return success;
    }

    public void logout() {
        client.get("http://www.shen-qi.com/webdo/logout.php");
        client.clearCookies();
    }

    public double getMoney() {
        double money = 0;

        String cashInfo = client.get("http://www.shen-qi.com/index.php");
        Document doc = Jsoup.parse(cashInfo);
        Elements cashElement = doc.select("div:contains(其中推广奖励)").select("h1").eq(1);
        if (null == cashElement) {
            return money;
        }

        money = NumberUtils.getDouble(cashElement.text());

        return money;
    }

    public boolean cash(int money) {
        boolean success;

        Map<String, String> cashParams = new HashMap<String, String>();
        cashParams.put("dhpoint", money + "");
        cashParams.put("type", "dhpoint");
        String cashRet = client.post("http://www.shen-qi.com/webdo/indexdo.php", cashParams, null, "http://www.shen-qi.com/index.php", "", null);
        if (!"success".equals(cashRet)) {
            success = false;
            return success;
        }

        success = true;

        return success;
    }
}
