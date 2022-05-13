package com.inview.rentserver.iface;

import com.inview.rentserver.pojo.DataEnum;
import person.inview.receiver.Receiver;

import java.lang.reflect.ParameterizedType;

public abstract class ReceiveListenerBase<T> {
    private Class<T> clazz;

    public ReceiveListenerBase() {
        clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public boolean register(Receiver receiver){
        if((DataEnum.getCode(clazz)& receiver.getDataCode())!=0){
           return notice(receiver);
        }
        return false;
    }

    public abstract boolean notice(Receiver receiver);
}
