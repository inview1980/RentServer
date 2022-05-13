package com.inview.rentserver.server;

import com.inview.rentserver.iface.ReceiveListenerBase;
import person.inview.receiver.Receiver;
import com.inview.rentserver.pojo.DataEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pojo.RentalRecord;

/**
 * 注解方式 @EventListener,监听用户注册事件
 *
 * @author Summerday
 */
@Service
@Slf4j
public class RentRecordDBNotice extends ReceiveListenerBase<RentalRecord> {
    /**
     * 监听用户注册事件,执行发放优惠券逻辑
     */
    @Override
    public boolean notice(Receiver receiver) {
        log.info("需修改的数据表：[{}]", DataEnum.RentRecord.name());
        return true;
    }
}
