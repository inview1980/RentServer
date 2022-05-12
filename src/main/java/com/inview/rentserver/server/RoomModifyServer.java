package com.inview.rentserver.server;

import person.inview.receiver.Receiver;
import com.inview.rentserver.iface.ReceiveListener;
import com.inview.rentserver.pojo.DataEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 实现ApplicationListener<Event>的方式,监听器
 *
 * @author Summerday
 */
@Service
@Slf4j
public class RoomModifyServer implements ReceiveListener {

    @Override
    public boolean register(Receiver receiver) {
        if ((receiver.getDataCode() & DataEnum.RoomDetails.getCode()) != 0) {
            log.info("需修改的数据表：[{}]", DataEnum.RoomDetails.name());
        }
        return true;
    }
}
