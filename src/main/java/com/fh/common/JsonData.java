package com.fh.common;
// 返回接口的统一数据规范
public class JsonData {

    //返回的状态码
    private Integer code;

    //返回的信息
    private String message;

    //返回的数据
    private Object data;

    //无参构造
    private JsonData(Object data){
        this.data=data;
        this.code=200;
        this.message="success";
    }



    private JsonData(String message){
        this.code=500;
        this.message=message;
    }

    private JsonData(Integer code,String message){
        this.code=code;
        this.message=message;
    }


    //静态方法
    public static JsonData getJsonSuccess(Object data){
        return  new JsonData(data);
    }

    public static JsonData getJsonError(String message){
        return new JsonData(message);
    }

    public static JsonData getJsonError(Integer code,String message){
        return new JsonData(code,message);
    }





    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
