package com.ruijc.zpp.task;

import com.ruijc.zpp.ZppProperties;
import com.ruijc.zpp.api.ZppApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(ZppProperties.class)
public class ZppTask {

    @Autowired
    private ZppApi api;
    @Autowired
    private ZppProperties properties;

    @Scheduled(cron = "0 30 9 * * ?")
    public void cash() {
        for (int i = 0; i < properties.getRetry(); ++i) {
            if (StringUtils.isAnyBlank(properties.getUsername(), properties.getPassword())) {
                return;
            }
            if (api.login(properties.getUsername(), properties.getPassword())) {
                break;
            }
        }

        for (int i = 0; i < properties.getRetry(); ++i) {
            if (api.cash(properties.getMinCash())) {
                break;
            }
        }

        api.logout();
    }
}
