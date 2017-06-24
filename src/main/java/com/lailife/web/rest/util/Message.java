package com.lailife.web.rest.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by SterOtto on 2017/5/28.
 */
public class Message<T> {
    /**
     * 返回码
     * @link ResponseStatus.value
     */
    private int code;
    /**
     * 信息说明
     * @link ResponseStatus.message
     */
    private String msg;
    /**
     * 返回数据
     */
    private T obj;

    public Message(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Message(int code, String msg, T obj) {
        this.code = code;
        this.msg = msg;
        this.obj = obj;
    }
    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Message fromResponseStatus(ResponseStatus responseStatus){
        return new Message(responseStatus.value(), responseStatus.message());
    }
    public static Message fromResponseStatus(ResponseStatus responseStatus, Object o){
        return new Message(responseStatus.value(), responseStatus.message(), o);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

}
