package com.inview.rentserver.server;

import com.alibaba.fastjson.JSONObject;
import com.inview.rentserver.base.ReceiveListenerBase;
import com.inview.rentserver.dao.RoomDao;
import com.inview.rentserver.pojo.DataEnum;
import com.inview.rentserver.tool.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import person.inview.receiver.Receiver;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.StrUtil;
import pojo.RentalRecord;
import pojo.RoomDetails;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
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
            case "changeRoomDelete":
                return moveToDelete(receiver.getData());
            default:
                return true;
        }
    }

    private boolean moveToDelete(String data) {
        if (StrUtil.hasBlank(data)) throw new MyException(WebResultEnum.ParameterError);
        try {
            Map map = JSONObject.parseObject(data, Map.class);
            if (map != null && map.containsKey("roomID") && map.containsKey("value")) {
                RoomDetails room = roomDao.findByID(Integer.parseInt(map.get("roomID").toString()));
                if (room != null) {
                    boolean value = Boolean.parseBoolean(map.get("value").toString());
                    room.setDelete(value);
                    roomDao.DBChanged();
                    return true;
                }
            }
            throw new MyException(WebResultEnum.ParameterError);
        } catch (Exception e) {
            throw new MyException(WebResultEnum.ParameterChangeError);
        }
    }


}
