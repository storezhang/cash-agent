package com.ruijc.zpp.task;

import com.ruijc.util.CollectionUtils;
import com.ruijc.util.StringUtils;
import com.ruijc.zpp.ZppProperties;
import com.ruijc.zpp.api.ZppApi;
import com.ruijc.zpp.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableConfigurationProperties(ZppProperties.class)
public class ZppTask {

    @Autowired
    private ZppApi api;
    @Autowired
    private ZppProperties properties;

    //@Scheduled(cron = "0 30 9 * * ?")
    @Scheduled(fixedRate = 1000)
    public void cash() {
        List<User> users = properties.getUsers();
        if (CollectionUtils.isBlank(users)) {
            return;
        }

        for (User user : users) {
            for (int i = 0; i < properties.getRetry(); ++i) {
                if (StringUtils.isAnyBlank(user.getUsername(), user.getPassword())) {
                    continue;
                }
                if (cash(user.getUsername(), user.getPassword())) {
                    break;
                }
            }
        }
    }

    private boolean cash(String username, String password) {
        boolean success;

        if (StringUtils.isAnyBlank(username, password)) {
            success = false;
            return success;
        }
        if (!api.login(username, password)) {
            success = false;
            return success;
        }

        double money = api.getMoney();
        if (money >= properties.getMinCash()) {
            success = api.cash((int) money);
        } else {
            success = false;
        }

        api.logout();

        return success;
    }
}
