package com.ruijc.cash;

import com.ruijc.cash.mayi.api.MayiApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CashAgentApplicationTests {

    @Autowired
    private MayiApi mayiApi;

    @Test
    public void testMayi() {
        mayiApi.login("storezhang", "xtkthh13284#%");
        mayiApi.getSurplusTixianTimes();
    }

}
