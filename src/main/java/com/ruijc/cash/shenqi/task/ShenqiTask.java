package com.ruijc.cash.shenqi.task;

import com.ruijc.cash.CashProperties;
import com.ruijc.cash.IfengApi;
import com.ruijc.cash.bean.User;
import com.ruijc.cash.shenqi.ShenqiProperties;
import com.ruijc.cash.shenqi.bean.Message;
import com.ruijc.cash.shenqi.process.ShenqiL;
import com.ruijc.cash.task.ITask;
import com.ruijc.util.CollectionUtils;
import com.ruijc.util.RandomUtils;
import com.ruijc.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@EnableConfigurationProperties({ShenqiProperties.class, CashProperties.class})
public class ShenqiTask implements ITask {

    @Autowired
    private ShenqiL shenqiL;
    @Autowired
    private ShenqiProperties shenqiProperties;
    @Autowired
    private CashProperties cashProperties;
    @Autowired
    private IfengApi ifengApi;

    @Override
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

    @Override
    @Scheduled(cron = "23 21 23 * * ?")
    public void confSystem() {
        List<String> words = ifengApi.getNews();
        if (CollectionUtils.isBlank(words)) {
            return;
        }

        Collections.shuffle(words);

        List<User> users = shenqiProperties.getUsers();
        if (CollectionUtils.isBlank(users)) {
            return;
        }

        for (User user : users) {
            for (int i = 0; i < cashProperties.getRetry(); ++i) {
                if (StringUtils.isAnyBlank(user.getUsername(), user.getPassword())) {
                    continue;
                }
                shenqiL.add(user.getUsername(), user.getPassword(), words.subList(0, words.size() / 5), Message.Type.MSG);
                shenqiL.add(user.getUsername(), user.getPassword(), words.subList(words.size() / 5 + 1, words.size() / 5 * 2), Message.Type.RES);
                shenqiL.add(user.getUsername(), user.getPassword(), words.subList(words.size() / 5 * 2 + 1, words.size() / 5 * 3), Message.Type.COMMENT);
                shenqiL.add(user.getUsername(), user.getPassword(), words.subList(words.size() / 5 * 3 + 1, words.size() / 5 * 4), Message.Type.QUN_MSG);
                shenqiL.add(user.getUsername(), user.getPassword(), words.subList(words.size() / 5 * 4 + 1, words.size()), Message.Type.QUN_RES);
            }
        }
    }
}
