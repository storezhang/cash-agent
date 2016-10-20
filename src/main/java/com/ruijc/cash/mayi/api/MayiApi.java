package com.ruijc.cash.mayi.api;

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
public class MayiApi {

    @Autowired
    private HttpClient client;

    public boolean login(String username, String password) {
        boolean success = false;

        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        String ret = client.post("http://www.mayiluntan.com/login/dologin.html", params, null, "http://www.mayiluntan.com", "", null);
        if (!"ok".equals(ret)) {
            success = false;
        } else {
            success = true;
        }

        return success;
    }

    public void logout() {
        client.get("http://member.zhuanpaopao.com/user/userout");
    }

    public double getMoney() {
        double money = 0;
        client.get("http://member.zhuanpaopao.com/tmplogin/index");
        String cashInfo = client.get("http://member.zhuanpaopao.com/user/user_cashinfo");
        Document doc = Jsoup.parse(cashInfo);
        Elements cashElement = doc.select("#neikuang div div span");
        if (null == cashElement) {
            return money;
        }

        money = NumberUtils.getDouble(cashElement.text());

        return money;
    }

    public boolean cash(int money) {
        boolean success;

        Map<String, String> cashParams = new HashMap<String, String>();
        cashParams.put("money", money + "");
        String cashRet = client.post("http://member.zhuanpaopao.com/user/cash", cashParams);
        if (!"ok".equals(cashRet)) {
            success = false;
            return success;
        }

        success = true;

        return success;
    }
}
