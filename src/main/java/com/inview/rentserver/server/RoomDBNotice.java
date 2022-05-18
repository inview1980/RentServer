package com.inview.rentserver.server;

import com.alibaba.fastjson.JSONObject;
import com.inview.rentserver.base.ReceiveListenerBase;
import com.inview.rentserver.dao.RoomDao;
import com.inview.rentserver.pojo.DataEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import person.inview.receiver.Receiver;
import pojo.RoomDetails;

import java.util.Map;

/**
 * 监听器
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RoomDBNotice extends ReceiveListenerBase<RoomDetails> {
    private final RoomDao roomDao;

    @Override
    public boolean notice(Receiver receiver) {
        switch (receiver.getOpcode()) {

            default:
                return true;
        }
    }


}
