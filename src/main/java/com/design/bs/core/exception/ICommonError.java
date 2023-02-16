package com.design.bs.core.exception;

/**
 * @description: 定义异常接口
 **/
public interface ICommonError {

    /**
     * 获取错误码
     * @return
     */
    String getCode();

    /**
     * 获取错误描述信息
     * @return
     */
    String getMsg();

}
