package com.fh.common;
//自定义异常
public class NologinException extends Exception {

    public NologinException(String message){
            super(message);
    }
}
