package com.ruijc.cash;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * 提现配置
 *
 * @author Storezhang
 */
@ConfigurationProperties(prefix = "cash")
public class CashProperties {

    private int retry;

    public CashProperties() {
        retry = 10;
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }
}
