package com.ruijc;

import com.ruijc.http.HttpClient;
import com.ruijc.util.MapUtils;
import com.ruijc.util.NumberUtils;
import com.ruijc.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.rmi.server.ExportException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class ZppAgentApplication implements CommandLineRunner {

    @Autowired
    private HttpClient client;

    public static void main(String[] args) {
        SpringApplication.run(ZppAgentApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        cash("storezhang@gmail.com", "xtkthh13284@$", 5);
    }

    private boolean cash(String username, String pwd, double minMoney) {
        boolean success = false;

        if (!client.downloadFile("http://www.zhuanpaopao.com/welcome/verifyCode", "code.jpg")) {
            return success;
        }
        String code = client.post("http://data.tehir.cn/url/Api/VCRInterface.ashx?apikey=646B7F4EB194A042E76E2615924FF84A&flag=zhuanpaopao", null, null, "", "img", new File("code.jpg"));
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid_mail", username);
        params.put("pwd", pwd);
        params.put("check_code", code);

        String ret = client.post("http://www.zhuanpaopao.com/welcome/login", params, null, "http://www.zhuanpaopao.com/", "", null);
        if (!"ok".equals(ret)) {
            return success;
        }

        client.get("http://member.zhuanpaopao.com/tmplogin/index");
        String cashInfo = client.get("http://member.zhuanpaopao.com/user/user_cashinfo");
        Document doc = Jsoup.parse(cashInfo);
        Elements cashElement = doc.select("#neikuang div div span");
        if (null == cashElement) {
            return success;
        }

        double money = NumberUtils.getDouble(cashElement.text());
        if (money >= minMoney) {
            Map<String, String> cashParams = new HashMap<String, String>();
            cashParams.put("money", money + "");
            String cashRet = client.post("http://member.zhuanpaopao.com/user/cash", cashParams);
            if (!"ok".equals(cashRet)) {
                return success;
            }
        }

        success = true;

        return success;
    }
}
