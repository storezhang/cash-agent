package com.ruijc.cash.zpp.task;

import com.ruijc.cash.CashProperties;
import com.ruijc.cash.zpp.ZppProperties;
import com.ruijc.cash.zpp.api.ZppApi;
import com.ruijc.cash.zpp.bean.ZppUser;
import com.ruijc.log.ILogger;
import com.ruijc.util.CollectionUtils;
import com.ruijc.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableConfigurationProperties({ZppProperties.class, CashProperties.class})
public class ZppTask {

    @Autowired
    private ZppApi api;
    @Autowired
    private ZppProperties zppProperties;
    @Autowired
    private CashProperties cashProperties;
    @Autowired
    private ILogger logger;

    @Scheduled(cron = "0 48 10 * * ?")
    public void cash() {
        List<ZppUser> users = zppProperties.getUsers();
        if (CollectionUtils.isBlank(users)) {
            return;
        }

        for (ZppUser user : users) {
            for (int i = 0; i < cashProperties.getRetry(); ++i) {
                if (StringUtils.isAnyBlank(user.getUsername(), user.getPassword())) {
                    continue;
                }
                int ret = cash(user.getUsername(), user.getPassword(), user.getType());
                switch (ret) {
                    case 1:
                    case -3:
                        return;
                    case -1:
                    case -2:
                        continue;
                }
            }
        }
    }

    private int cash(String username, String password, String type) {
        int ret = 0;

        if (StringUtils.isAnyBlank(username, password)) {
            ret = -1;

            logger.log(ZppProperties.LOG_STORE, ZppProperties.LOG_TOP_LOGIN, "", "success", false, "msg", "用户名或者密码为空！");

            return ret;
        }
        if (!api.login(username, password)) {
            ret = -2;

            logger.log(ZppProperties.LOG_STORE, ZppProperties.LOG_TOP_LOGIN, "", "success", false, "msg", "登录失败！");

            return ret;
        }

        double money = api.getMoney();
        if (money >= zppProperties.getMinCash()) {
            String realType = "1";
            switch (type) {
                case "ALIPAY":
                    realType = "1";
                    break;
                case "BANK":
                    realType = "2";
                    break;
            }
            if (api.cash((int) money, realType)) {
                ret = 1;
                logger.log(ZppProperties.LOG_STORE, ZppProperties.LOG_TOP_CASH, "", "success", true, "money", (int) money, "msg", "提现成功！");
            } else {
                ret = -2;
            }
        } else {
            ret = -3;

            logger.log(ZppProperties.LOG_STORE, ZppProperties.LOG_TOP_CASH, "", "success", false, "money", money, "msg", "提现失败，余额不足！");
        }

        api.logout();

        return ret;
    }
}
