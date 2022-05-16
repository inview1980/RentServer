package com.inview.rentserver.server;

import com.inview.rentserver.base.ReceiveListenerBase;
import org.springframework.stereotype.Component;
import person.inview.receiver.Receiver;
import pojo.DebtRecord;

@Component
public class DebtRecordDBNotice extends ReceiveListenerBase<DebtRecord> {
    @Override
    public boolean notice(Receiver receiver) {
        return false;
    }
}
