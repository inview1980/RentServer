package com.inview.rentserver.server;

import com.inview.rentserver.base.ReceiveListenerBase;
import org.springframework.stereotype.Component;
import person.inview.receiver.Receiver;
import pojo.CarMaintenanceRecord;

@Component
public class CarRecordDBNotice extends ReceiveListenerBase<CarMaintenanceRecord> {
    @Override
    public boolean notice(Receiver receiver) {
        return false;
    }
}
