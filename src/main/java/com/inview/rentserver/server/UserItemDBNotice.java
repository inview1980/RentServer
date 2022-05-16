package com.inview.rentserver.server;

import com.inview.rentserver.base.ReceiveListenerBase;
import org.springframework.stereotype.Component;
import person.inview.receiver.Receiver;
import pojo.UserItem;

@Component
public class UserItemDBNotice extends ReceiveListenerBase<UserItem> {
    @Override
    public boolean notice(Receiver receiver) {
        return false;
    }
}
