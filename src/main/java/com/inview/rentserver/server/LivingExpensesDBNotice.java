package com.inview.rentserver.server;

import com.inview.rentserver.base.ReceiveListenerBase;
import org.springframework.stereotype.Component;
import person.inview.receiver.Receiver;
import pojo.LivingExpenses;

@Component
public class LivingExpensesDBNotice extends ReceiveListenerBase<LivingExpenses> {
    @Override
    public boolean notice(Receiver receiver) {
        return false;
    }
}
