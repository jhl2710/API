package com.fh.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//springmvc的全局异常处理
@ControllerAdvice
public class ControllerException {


    /**
     * 处理所有不可知异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonData handleException(Exception e) {
        return JsonData.getJsonError(e.getMessage());
    }

    //未登录异常
    @ExceptionHandler(NologinException.class)
    @ResponseBody
    public JsonData handleNoLoginException(NologinException e){
        return JsonData.getJsonError(1000,e.getMessage());
    }

    //库存不够异常
    @ExceptionHandler(CountException.class)
    @ResponseBody
    public JsonData handleCountException(CountException e){
        return JsonData.getJsonError(2000,e.getMessage());
    }




}
