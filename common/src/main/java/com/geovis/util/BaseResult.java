package com.geovis.util;


import java.io.Serializable;

/**
 * 统一返回结果类
 */
public class BaseResult implements Serializable {

	private static final long serialVersionUID = 1L;

	// 状态码
    public int code;

    // 成功为success，其他为失败原因
    public String message;

    // 数据结果集
    public Object data;

    public BaseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public BaseResult(HttpStatus httpStatus, Object data) {
    	this.code = httpStatus.getStatusCode();
        this.message = httpStatus.toString();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
