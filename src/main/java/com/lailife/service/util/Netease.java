package com.lailife.service.util;

/**
 * Created by SterOtto on 2017/4/26.
 */
public class Netease {
    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String OBJ = "obj";

    public static final int CODE_OK = 200;
    private int code;
    private String msg;
    private String obj;

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

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }
}
