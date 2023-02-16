package com.design.bs.core.exception;

import java.text.MessageFormat;

/**
 * @description: 业务异常
 **/
public class BusinessException extends RuntimeException implements ICommonError {

    private static final long serialVersionUID = -146065123620309427L;
    protected String msg;
    protected String code;

    public BusinessException(ICommonError error) {
        this.code = error.getCode();
        this.msg = error.getMsg();
    }

    public BusinessException(ICommonError error, String otherMsg, Object overload) {
        this.code = error.getCode();
        this.msg = otherMsg + error.getMsg();
    }

    public BusinessException(ICommonError error, Object... arguments) {
        this.code = error.getCode();
        this.msg = MessageFormat.format(error.getMsg(), arguments);
    }

    public BusinessException(ICommonError error, Throwable e) {
        super(e);
        this.code = error.getCode();
        this.msg = error.getMsg();
    }

    /**
     * 当 ICommonError 中的msg不能符合要求时，
     * 使用本构造函数对msg重新赋值
     * @param error
     * @param msg
     */
    public BusinessException(ICommonError error, String msg) {
        this.code = error.getCode();
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

}
