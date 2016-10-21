package com.ruijc.cash.tingyun.api;

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
 * 蚂蚁网页挂机工具类
 *
 * @author Storezhang
 */
@Service
public class TingyunApi {

    @Autowired
    private HttpClient client;

    public boolean login(String username, String password) {
        boolean success = false;

        if (!client.downloadFile("http://member.tingyun.com/member/user/getImage", "tingyun-code.jpg")) {
            success = false;
            return success;
        }
        String code = client.post("http://data.tehir.cn/url/Api/VCRInterface.ashx?apikey=646B7F4EB194A042E76E2615924FF84A&flag=tingyun", null, null, "", "img", new File("tingyun-code.jpg"));
        Map<String, String> params = new HashMap<String, String>();
        params.put("loginName", username);
        params.put("password", password);
        params.put("captcha", code);

        String ret = client.post("http://member.tingyun.com/member/login", params, null, "http://www.tingyun.com", "", null);
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

    public void logout() {
        client.clearCookies();
    }

    public boolean cash(int money) {
        boolean success;

        Map<String, String> cashParams = new HashMap<String, String>();
        cashParams.put("prize", money + "");
        String cashRet = client.post("http://member.tingyun.com/member/member/memberApplyPrize", cashParams);
        if (!cashRet.contains("提现成功")) {
            success = false;
            return success;
        }

        success = true;

        return success;
    }
}
