package com.inview.rentserver.server;

import com.alibaba.fastjson.JSONObject;
import com.inview.rentserver.base.ReceiveListenerBase;
import com.inview.rentserver.dao.RentalRecordDao;
import com.inview.rentserver.pojo.DataEnum;
import com.inview.rentserver.tool.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import person.inview.receiver.Receiver;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.StrUtil;
import pojo.RentalRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RentRecordDBNotice extends ReceiveListenerBase<RentalRecord> {
    private final RentalRecordDao rentalRecordDao;

    @Override
    public boolean notice(Receiver receiver) {
        log.info("需修改的数据表：[{}]", DataEnum.RentRecord.name());
        switch (receiver.getOpcode()) {
            case "changeRoomDeposit"://修改押金
                return changeRoomDeposit(receiver.getData());
        }
        return true;
    }

    /**
     * 修改押金
     * @param data json序列化的Map字符串
     * @return 修改成功返回true
     */
    private boolean changeRoomDeposit(String data) {
        if (StrUtil.hasBlank(data)) throw new MyException(WebResultEnum.ParameterError);
        try {
            Map map = JSONObject.parseObject(data, Map.class);
            if (map != null && map.containsKey("recordID") && map.containsKey("value")) {
                RentalRecord rr = rentalRecordDao.findByID(Integer.parseInt( map.get("recordID").toString()));
                if (rr != null) {
                    BigDecimal value = new BigDecimal(map.get("value").toString());
                    rr.setDeposit(value);
                    rentalRecordDao.DBChanged();
                    return true;
                }
            }
            throw new MyException(WebResultEnum.ParameterError);
        } catch (Exception e) {
            throw new MyException(WebResultEnum.ParameterChangeError);
        }
    }
}
