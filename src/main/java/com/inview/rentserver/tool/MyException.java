package com.inview.rentserver.tool;

import person.inview.receiver.WebResultEnum;

import java.util.function.Supplier;

public  class  MyException extends RuntimeException implements Supplier<MyException> {
    private int code;

    public MyException(int code, String codeMsg) {
        super(codeMsg);
        this.code = code;

    }

    public MyException(WebResultEnum resultEnum){
        super(resultEnum.getCodeMsg());
        this.code=resultEnum.getCode();
    }

    @Override
    public MyException get() {
        return this;
    }

    public int getCode() {
        return code;
    }

    public MyException() {
    }
}
