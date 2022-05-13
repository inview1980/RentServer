package com.inview.rentserver.dao;

import com.inview.rentserver.base.DBBase;
import org.springframework.stereotype.Component;
import pojo.PayProperty;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PayPropertyDao extends DBBase<PayProperty> {
    /**
     * 获取所有指定房间号的物业缴费记录
     * @param roomNumber 房间号
     */
    public List<PayProperty> findByRoomNumber(String roomNumber) {
        return getDB().stream().filter(payProperty -> roomNumber.equals(payProperty.getRoomNumber()))
                .sorted(Comparator.comparing(PayProperty::getStartDate))
                .collect(Collectors.toList());
    }
}
