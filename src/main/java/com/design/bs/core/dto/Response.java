package com.design.bs.core.dto;

import com.design.bs.core.enums.StatusCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 响应封装类
 **/
@Data
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -1252062355406864129L;

    private String code;

    private String msg;

    private T data;

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }
    
    public Response(StatusCode statusCode, String otherMsg, Object overload) {
    	this.code = statusCode.getCode();
    	this.msg = otherMsg+statusCode.getMsg();
    }

    public Response(StatusCode statusCode, T data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }
}
