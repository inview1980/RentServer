package com.inview.rentserver.event;

import com.inview.rentserver.base.ReceiveListenerBase;
import person.inview.receiver.Receiver;
import com.inview.rentserver.tool.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 广播的发布者，自动扫描实现ReceiveListener接口的Bean
 */
@Component
@Slf4j
public class ModifyFactory {
    private static Collection<ReceiveListenerBase> receiveList = null;
    private static final Object object = new Object();

    /**
     * 发布事件，通知所有listener
     */
    public boolean notice(Receiver receiver) {
        log.info("执行的修改代码为：[{}]", receiver.getDataCode());
        if (ModifyFactory.receiveList == null) {
            initListener();
        }
        boolean y = true;
        for (ReceiveListenerBase listener : receiveList) {
            if (!listener.register(receiver)) {
                y = false;
            }
        }
        return y;
    }

    /**
     * 获取所有实现ReceiveListener接口的Bean
     */
    private void initListener() {
        synchronized (ModifyFactory.object) {
            ModifyFactory.receiveList = SpringBeanUtil.getApplicationContext().getBeansOfType(ReceiveListenerBase.class).values();
        }
        log.info("获取所有实现ReceiveListener接口的Bean，共计[{}]",ModifyFactory.receiveList.size());
    }

}
