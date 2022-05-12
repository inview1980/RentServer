package com.inview.rentserver.server;

import person.inview.receiver.Receiver;
import com.inview.rentserver.iface.ReceiveListener;
import com.inview.rentserver.pojo.DataEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 注解方式 @EventListener,监听用户注册事件
 * @author Summerday
 */
@Service
@Slf4j
public class RentRecordModifyServer implements ReceiveListener {
    /**
     * 监听用户注册事件,执行发放优惠券逻辑
     */
    @Override
    public boolean register(Receiver receiver) {
        if ((receiver.getDataCode() & DataEnum.RentRecord.getCode()) != 0) {
            log.info("需修改的数据表：[{}]", DataEnum.RentRecord.name());
        }
        return true;
    }
}
