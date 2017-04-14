package com.ruijc.cash.shenqi.process;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                      .   ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                  佛祖镇楼                  BUG辟易
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；
//                  不见满街漂亮妹，哪个归得程序员？

import com.ruijc.cash.shenqi.ShenqiProperties;
import com.ruijc.cash.shenqi.api.ShenqiApi;
import com.ruijc.cash.shenqi.bean.Message;
import com.ruijc.log.ILogger;
import com.ruijc.util.CollectionUtils;
import com.ruijc.util.RandomUtils;
import com.ruijc.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 神器逻辑
 *
 * @author Storezhang
 * @create 2017-04-13 16:27
 * @email storezhang@gmail.com
 * @qq 160290688
 */
@Service
public class ShenqiL {

    @Autowired
    private ShenqiApi api;
    @Autowired
    private ShenqiProperties shenqiProperties;
    @Autowired
    private ILogger logger;

    public boolean add(String username, String password, List<String> msgs, Message.Type type) {
        logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_MSG, "", "success", true, "type", type.getType(), "content", msg, "msg", "添加消息。");

        boolean success = true;

        if (StringUtils.isAnyBlank(username, password)) {
            success = false;

            logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_LOGIN, "", "success", false, "username", username, "msg", "用户名或者密码为空！");

            return success;
        }
        if (!api.login(username, password)) {
            success = false;

            logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_LOGIN, "", "success", false, "username", username, "msg", "登录失败！");

            return success;
        }

        List<Message> nowMessages = api.getMessages(type);
        while (!CollectionUtils.isBlank(nowMessages)) {
            for (Message msg : nowMessages) {
                success = api.delete(msg) && success;
                try {
                    TimeUnit.SECONDS.sleep(RandomUtils.nextInt(10));
                } catch (Exception e) {
                    logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_MSG, "", "success", false, "username", username, "msg", "停顿失败！");
                }
                logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_MSG, "", "success", true, "type", type.getType(), "content", msg.getMsg(), "msg", "删除消息。");
            }
            nowMessages = api.getMessages(type);
        }

        if (msgs.size() > shenqiProperties.getMaxMsgSize()) {
            msgs = msgs.subList(0, shenqiProperties.getMaxMsgSize());
        }

        for (String msg : msgs) {
            success = api.add(type, msg) && success;
            try {
                TimeUnit.SECONDS.sleep(RandomUtils.nextInt(10));
            } catch (Exception e) {
                logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_MSG, "", "success", false, "username", username, "msg", "停顿失败！");
            }
            logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_MSG, "", "success", true, "type", type.getType(), "content", msg, "msg", "添加消息。");
        }

        return success;
    }


    public int cash(String username, String password) {
        int ret;

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
        int rate = shenqiProperties.getRate();
        int rmb = (int) (money / rate);
        if (rmb >= shenqiProperties.getMinCash()) {
            if (api.cash(rmb * rate)) {
                ret = 1;
                logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_MSG, "", "success", true, "money", rmb * rate, "username", username, "msg", "提现成功！");
            } else {
                ret = -2;
            }
        } else {
            ret = -3;

            logger.log(ShenqiProperties.LOG_STORE, ShenqiProperties.LOG_TOP_MSG, "", "success", false, "money", money, "username", username, "msg", "提现失败，余额不足！");
        }

        api.logout();

        return ret;
    }
}
