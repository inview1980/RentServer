package com.inview.rentserver.server;

import com.inview.rentserver.base.ReceiveListenerBase;
import org.springframework.stereotype.Component;
import person.inview.receiver.Receiver;
import pojo.ShoppingRecord;

@Component
public class ShoppingRecordDBNotice extends ReceiveListenerBase<ShoppingRecord> {
    @Override
    public boolean notice(Receiver receiver) {
        return false;
    }
}
