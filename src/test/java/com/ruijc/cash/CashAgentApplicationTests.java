package com.ruijc.cash;

import com.ruijc.cash.shenqi.api.ShenqiApi;
import com.ruijc.cash.shenqi.bean.Message;
import com.ruijc.cash.shenqi.task.ShenqiTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CashAgentApplicationTests {

    @Autowired
    private ShenqiTask task;
    @Autowired
    private ShenqiApi api;

    @Test
    public void testShenqiCash() {
        task.cash();
    }

    @Test
    public void testShenqiWords() {
        task.addWords();
    }
}
