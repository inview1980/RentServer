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
            case "notRented":
                return notRented(receiver.getData());
            default:
                return true;
        }
    }

    /**
     * 退租
     *
     * @param data 出租记录表的ID
     */
    private boolean notRented(String data) {
        if (StrUtil.hasBlank(data)) throw new MyException(WebResultEnum.ParameterError);
        try {
            int rentID = Integer.parseInt(data);
            RoomDetails room = roomDao.findByID(rentID);
            if (room != null) {
                room.setRecordId(0);
                roomDao.DBChanged();
                return true;
            }
            throw new MyException(WebResultEnum.ParameterError);
        } catch (Exception e) {
            throw new MyException(WebResultEnum.ParameterChangeError);
        }
    }

    /**
     * 设置指定房间是否处于“不可示”状态
     *
     * @param data Map的序列化字符串，包含key:roomID\value
     */
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
