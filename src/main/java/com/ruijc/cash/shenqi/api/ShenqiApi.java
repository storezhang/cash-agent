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


    public boolean login(String username, String password, String successRet) {
        boolean success = false;

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
        if (!successRet.trim().equals(ret.trim())) {//﻿<script language='javascript'> location='/index.php';</script>
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
        Elements cashElement = doc.select("#dhModal div div .modal-body p font");
        if (null == cashElement) {
            return money;
        }

        money = NumberUtils.getDouble(cashElement.text());

        return money;
    }

    public int getId() {
        int money = 0;

        String idInfo = client.get("http://www.shen-qi.com/index.php");
        Document doc = Jsoup.parse(idInfo);
        Elements idElement = doc.select("#myid");
        if (null == idElement) {
            return money;
        }

        money = NumberUtils.getInt(idElement.text());

        return money;
    }

    public int getRate() {
        int rate = 0;

        String rateInfo = client.get("http://www.shen-qi.com/index.php");
        Document doc = Jsoup.parse(rateInfo);
        Elements rateElement = doc.select("#dhpoint");
        if (null == rateElement) {
            return rate;
        }

        rate = NumberUtils.getInt(rateElement.attr("placeholder").split(",")[1].split(":")[0]);

        return rate;
    }

    public boolean cash(int id, int money) {
        boolean success;

        Map<String, String> cashParams = new HashMap<String, String>();
        cashParams.put("dhpoint", money + "");
        cashParams.put("type", "dhpoint");
        cashParams.put("uid", id + "");
        String cashRet = client.post("http://www.shen-qi.com/webdo/indexdo.php", cashParams, null, "http://www.shen-qi.com/index.php", "", null);
        if (!"success".equals(cashRet)) {
            success = false;
            return success;
        }

        success = true;

        return success;
    }
}
