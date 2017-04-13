package com.ruijc.cash.shenqi.task;

import com.ruijc.cash.CashProperties;
import com.ruijc.cash.IfengApi;
import com.ruijc.cash.bean.User;
import com.ruijc.cash.shenqi.ShenqiProperties;
import com.ruijc.cash.shenqi.bean.Message;
import com.ruijc.cash.shenqi.process.ShenqiL;
import com.ruijc.util.CollectionUtils;
import com.ruijc.util.RandomUtils;
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
    private ShenqiL shenqiL;
    @Autowired
    private ShenqiProperties shenqiProperties;
    @Autowired
    private CashProperties cashProperties;
    @Autowired
    private IfengApi ifengApi;

    @Scheduled(cron = "11 01 11 * * ?")
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
                int ret = shenqiL.cash(user.getUsername(), user.getPassword());
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


    @Scheduled(cron = "23 21 23 * * ?")
    public void addWords() {
        List<String> words = ifengApi.getNews();
        if (CollectionUtils.isBlank(words)) {
            return;
        }

        List<User> users = shenqiProperties.getUsers();
        if (CollectionUtils.isBlank(users)) {
            return;
        }

        for (User user : users) {
            for (int i = 0; i < cashProperties.getRetry(); ++i) {
                if (StringUtils.isAnyBlank(user.getUsername(), user.getPassword())) {
                    continue;
                }
                shenqiL.add(user.getUsername(), user.getPassword(), words.subList(0, words.size() / 4), Message.Type.MSG);
                /*shenqiL.add(user.getUsername(), user.getPassword(), RandomUtils.randomList(words, 50), Message.Type.RES);
                shenqiL.add(user.getUsername(), user.getPassword(), RandomUtils.randomList(words, 50), Message.Type.COMMENT);
                shenqiL.add(user.getUsername(), user.getPassword(), RandomUtils.randomList(words, 50), Message.Type.QUN_MSG);
                shenqiL.add(user.getUsername(), user.getPassword(), RandomUtils.randomList(words, 50), Message.Type.QUN_RES);*/
            }
        }
    }
}
