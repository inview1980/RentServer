package com.inview.rentserver.iface;

import person.inview.receiver.Receiver;

public interface ReceiveListener {
    /**
     * 发布事件
     */
    boolean register(Receiver receiver);
}
