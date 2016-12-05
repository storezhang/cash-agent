package com.ruijc.cash.shenqi.task;

import com.ruijc.cash.CashProperties;
import com.ruijc.cash.bean.User;
import com.ruijc.cash.shenqi.ShenqiProperties;
import com.ruijc.cash.shenqi.api.ShenqiApi;
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
@EnableConfigurationProperties({ShenqiProperties.class, CashProperties.class})
public class ShenqiTask {

    @Autowired
    private ShenqiApi api;
    @Autowired
    private ShenqiProperties shenqiProperties;
    @Autowired
    private CashProperties cashProperties;
    @Autowired
    private ILogger logger;

    @Scheduled(cron = "18 58 15 * * ?")
    public void cash() {
        List<User> users = shenqiProperties.getUsers();
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

            logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_LOGIN, "", "success", false, "username", username, "msg", "用户名或者密码为空！");

            return ret;
        }
        if (!api.login(username, password)) {
            ret = -2;

            logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_LOGIN, "", "success", false, "username", username, "msg", "登录失败！");

            return ret;
        }

        double money = api.getMoney();
        int rate = api.getRate();
        int rmb = (int) (money / rate);
        if (rmb >= shenqiProperties.getMinCash()) {
            if (api.cash(api.getId(), rmb * rate)) {
                ret = 1;
                logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_CASH, "", "success", true, "money", rmb * rate, "username", username, "msg", "提现成功！");
            } else {
                ret = -2;
            }
        } else {
            ret = -3;

            logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_CASH, "", "success", false, "money", money, "username", username, "msg", "提现失败，余额不足！");
        }

        api.logout();

        return ret;
    }
}
