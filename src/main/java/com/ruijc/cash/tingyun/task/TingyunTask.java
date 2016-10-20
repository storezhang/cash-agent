package com.ruijc.cash.tingyun.task;

import com.ruijc.cash.CashProperties;
import com.ruijc.cash.bean.User;
import com.ruijc.cash.tingyun.TingyunProperties;
import com.ruijc.cash.tingyun.api.TingyunApi;
import com.ruijc.log.ILogger;
import com.ruijc.util.CollectionUtils;
import com.ruijc.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableConfigurationProperties({TingyunProperties.class, CashProperties.class})
public class TingyunTask {

    @Autowired
    private TingyunApi api;
    @Autowired
    private TingyunProperties mayiProperties;
    @Autowired
    private CashProperties cashProperties;
    @Autowired
    private ILogger logger;

    @Scheduled(cron = "0 48 10 * * ?")
    public void cash() {
        List<User> users = mayiProperties.getUsers();
        if (CollectionUtils.isBlank(users)) {
            return;
        }

        for (User user : users) {
            for (int i = 0; i < cashProperties.getRetry(); ++i) {
                if (StringUtils.isAnyBlank(user.getUsername(), user.getPassword())) {
                    continue;
                }
                int ret = cash(user.getUsername(), user.getPassword());
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

    private int cash(String username, String password) {
        int ret = 0;

        if (StringUtils.isAnyBlank(username, password)) {
            ret = -1;

            logger.log(TingyunProperties.LOG_STORE, TingyunProperties.LOG_TOP_LOGIN, "", "success", false, "msg", "用户名或者密码为空！");

            return ret;
        }
        if (!api.login(username, password)) {
            ret = -2;

            logger.log(TingyunProperties.LOG_STORE, TingyunProperties.LOG_TOP_LOGIN, "", "success", false, "msg", "登录失败！");

            return ret;
        }

        int surplus = api.getSurplusTixianTimes();
        if (surplus < 1) {
            logger.log(TingyunProperties.LOG_STORE, TingyunProperties.LOG_TOP_CASH, "", "success", false, "msg", "提现次数不足！");

            ret = -3;
            return ret;
        }
        double money = api.getMoney();
        if (money >= mayiProperties.getMinCash()) {
            if (api.cash((int) money)) {
                ret = 1;
                logger.log(TingyunProperties.LOG_STORE, TingyunProperties.LOG_TOP_CASH, "", "success", true, "money", (int) money, "msg", "提现成功！");
            } else {
                ret = -2;
            }
        } else {
            ret = -3;

            logger.log(TingyunProperties.LOG_STORE, TingyunProperties.LOG_TOP_CASH, "", "success", false, "money", money, "msg", "提现失败，余额不足！");
        }

        api.logout();

        return ret;
    }
}
