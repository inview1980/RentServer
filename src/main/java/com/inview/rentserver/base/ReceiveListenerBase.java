package com.inview.rentserver.base;

import com.inview.rentserver.pojo.DataEnum;
import person.inview.receiver.Receiver;

import java.lang.reflect.ParameterizedType;

/**
 * 广播接收器基类
 * @param <T> 取值范围为基础数据类，如 {@link pojo.RoomDetails}
 * 且与枚举 {@link DataEnum}中的值对应，如枚举中无此值，相应的接收器将不会收到消息
 */
public abstract class ReceiveListenerBase<T> {
    private Class<T> clazz;

    public ReceiveListenerBase() {
        clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public boolean register(Receiver receiver){
        if((DataEnum.getCode(clazz.getSimpleName())& receiver.getDataCode())!=0){
           return notice(receiver);
        }
        return true;
    }

    public abstract boolean notice(Receiver receiver);
}
