package com.inview.rentserver.server;

import com.inview.rentserver.base.ReceiveListenerBase;
import org.springframework.stereotype.Component;
import person.inview.receiver.Receiver;
import pojo.PersonDetails;

@Component
public class PersonDBNotice extends ReceiveListenerBase<PersonDetails> {
    @Override
    public boolean notice(Receiver receiver) {
        return false;
    }
}
