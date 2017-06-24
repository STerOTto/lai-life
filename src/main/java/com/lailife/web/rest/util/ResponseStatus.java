package com.lailife.web.rest.util;


/**
 * Created by SterOtto on 2017/5/29.
 */
public enum ResponseStatus {
    ERROR(-4, "error"),
    WRONG(-3, "wrong"),
    NULL(-2, "null"),
    FAILED(-1, "failed"),
    UNKNOWN(0, "unknown"),
    SUCCESS(1, "success"),
    EXIST(2,"exist"),
    NOT_EXIST(3, "not exist"),
    IN_USE(4, "in use"),
    DELETED(9,"deleted");

    /**
     * 返回码
     */
    private final int value;
    /**
     * 返回消息
     */
    private final String message;

    ResponseStatus(int value, String message) {
        this.value = value;
        this.message = message;
    }
    public int value(){
        return this.value;
    }
    public String message(){
        return this.message;
    }
    public static ResponseStatus valueOf(int statusCode){
        for (ResponseStatus responseStatus : values()){
            if(statusCode == responseStatus.value)
                return responseStatus;
        }
        throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
    }
}
