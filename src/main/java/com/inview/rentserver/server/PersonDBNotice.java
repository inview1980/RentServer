package com.inview.rentserver.server;

import com.alibaba.fastjson.JSONObject;
import com.inview.rentserver.base.ReceiveListenerBase;
import org.springframework.stereotype.Component;
import person.inview.receiver.Receiver;
import pojo.PersonDetails;

import java.util.Map;

@Component
public class PersonDBNotice extends ReceiveListenerBase<PersonDetails> {
    @Override
    public boolean notice(Receiver receiver) {
        switch (receiver.getOpcode()) {

            default:
                return true;
        }
    }
}
