package com.inview.rentserver.server;

import com.inview.rentserver.iface.ReceiveListenerBase;
import person.inview.receiver.Receiver;
import com.inview.rentserver.pojo.DataEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pojo.FuelRecord;

/**
 * 实现ApplicationListener<Event>的方式,监听器
 *
 * @author Summerday
 */
@Service
@Slf4j
public class FuelDBNotice extends ReceiveListenerBase<FuelRecord> {


    @Override
    public boolean notice(Receiver receiver) {
        log.info("需修改的数据表：[{}]", DataEnum.FuelRecord.name());
        return true;
    }
}
