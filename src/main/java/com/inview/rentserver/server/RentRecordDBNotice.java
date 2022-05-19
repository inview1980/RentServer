package com.inview.rentserver.server;

import com.alibaba.fastjson.JSONObject;
import com.inview.rentserver.base.ReceiveListenerBase;
import com.inview.rentserver.dao.RentalRecordDao;
import com.inview.rentserver.tool.MyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import person.inview.iface.IClassName;
import person.inview.receiver.Receiver;
import person.inview.receiver.WebResultEnum;
import person.inview.tools.StrUtil;
import pojo.RentalRecord;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class RentRecordDBNotice extends ReceiveListenerBase<RentalRecord> {
    private final RentalRecordDao rentalRecordDao;

    @Override
    public boolean notice(Receiver receiver) {
        switch (receiver.getOpcode()) {
            case "changeRoomDeposit"://修改押金
                return changePojoValueByBigDecimal(receiver.getData(), (new RentalRecord())::setDeposit);
            case "changeMonthlyRent"://修改租金:
                return changePojoValueByBigDecimal(receiver.getData(), new RentalRecord()::setMonthlyRent);
        }
        return true;
    }


    /**
     * 利用反射获取相应的set方法，设置属性值
     *
     * @param methodName 方法名
     */
    private boolean changePojoValueByBigDecimal(String data, IClassName<BigDecimal> methodName) {
        if (StrUtil.hasBlank(data)) throw new MyException(WebResultEnum.ParameterError);
        try {
            Map map = JSONObject.parseObject(data, Map.class);
            if (map != null && map.containsKey("recordID") && map.containsKey("value")) {
                RentalRecord rentalRecord = rentalRecordDao.findByID(Integer.parseInt(map.get("recordID").toString()));
                if (rentalRecord != null) {
                    BigDecimal value = new BigDecimal(map.get("value").toString());

                    Method set = RentalRecord.class.getMethod(methodName.getImplMethodName(), BigDecimal.class);
                    set.invoke(rentalRecord, value);
                    rentalRecordDao.DBChanged();
                    return true;
                }
            }
            throw new MyException(WebResultEnum.ParameterError);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new MyException(11000, "反射获取方法或设置属性值时异常");
        } catch (Exception e) {
            throw new MyException(WebResultEnum.ParameterChangeError);
        }
    }
}
