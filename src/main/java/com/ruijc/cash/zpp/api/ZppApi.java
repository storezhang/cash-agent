package com.ruijc.cash.zpp.api;

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
 * 赚泡泡工具类
 *
 * @author Storezhang
 */
@Service
public class ZppApi {

    @Autowired
    private HttpClient client;

    public boolean login(String username, String password) {
        boolean success = false;

        if (!client.downloadFile("http://www.zhuanpaopao.com/welcome/verifyCode", "zpp-code.jpg")) {
            success = false;
            return success;
        }
        //http://data.tehir.cn/url/Api/VCRInterface.ashx?apikey=646B7F4EB194A042E76E2615924FF84A&flag=zhuanpaopao&ImgUrl=http://www.zhuanpaopao.com/welcome/verifyCode
        String code = client.post("http://data.tehir.cn/url/Api/VCRInterface.ashx?apikey=646B7F4EB194A042E76E2615924FF84A&flag=zhuanpaopao", null, null, "", "img", new File("zpp-code.jpg"));
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid_mail", username);
        params.put("pwd", password);
        params.put("check_code", code);

        String ret = client.post("http://www.zhuanpaopao.com/welcome/login", params, null, "http://www.zhuanpaopao.com/", "", null);
        if (!"ok".equals(ret)) {
            success = false;
        } else {
            success = true;
        }

        return success;
    }

    public void logout() {
        client.get("http://member.zhuanpaopao.com/user/userout");
        client.clearCookies();
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

    public boolean cash(int money, String type) {
        boolean success;

        Map<String, String> cashParams = new HashMap<String, String>();
        cashParams.put("money", money + "");
        cashParams.put("checktype", type);
        String cashRet = client.post("http://member.zhuanpaopao.com/user/cash", cashParams);
        if (!"ok".equals(cashRet)) {
            success = false;
            return success;
        }

        success = true;

        return success;
    }
}
