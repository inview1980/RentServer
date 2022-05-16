package com.inview.rentserver.server;

import com.inview.rentserver.base.ReceiveListenerBase;
import org.springframework.stereotype.Component;
import person.inview.receiver.Receiver;
import pojo.PayProperty;

@Component
public class PayPropertyDBNotice extends ReceiveListenerBase<PayProperty> {
    @Override
    public boolean notice(Receiver receiver) {
        return false;
    }
}
