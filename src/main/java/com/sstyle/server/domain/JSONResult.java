package com.sstyle.server.domain;


/**
 * Created by ss on 18/3/10.
 */
public class JSONResult {
    private String code = "0";
    private String message = "请求成功";
    private Object data;


    public JSONResult() {
    }

    public JSONResult(Object data) {
        this.data = data;
    }

    public JSONResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
