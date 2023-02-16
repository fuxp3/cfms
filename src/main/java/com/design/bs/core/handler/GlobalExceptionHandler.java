package com.design.bs.core.handler;

import com.design.bs.core.dto.Response;
import com.design.bs.core.enums.StatusCode;
import com.design.bs.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description: 异常处理通知
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 方法参数校验不通过产生的异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        /**
         * 请求参数到方法参数的绑定过程中，如校验参数不合法，
         * 就会将所有的错误信息放在MethodArgumentNotValidException的BindingResult对象中，
         */
        StringBuilder msgs = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            msgs.append(",").append(fieldError.getDefaultMessage());
        });

        log.debug("请求参数校验异常:{}", msgs.toString().substring(1));
        return new Response(StatusCode.REQUEST_PARAM_ERROR.getCode(), msgs.toString().substring(1));
    }

    /**
     * 请求参数绑定到VO对象产生异常时的处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response handBindException(BindException e) {
        /**
         * 请求参数（json字符串）到方法参数（VO对象）的绑定过程中，如校验参数不合法，
         * 就会将所有的错误信息放在BindException的BindingResult对象中，
         */
        StringBuilder msgs = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(objectError -> {
            msgs.append(",").append(objectError.getDefaultMessage());
        });

        log.debug("请求参数绑定时校验异常:{}", msgs.toString().substring(1));
        return new Response(StatusCode.REQUEST_PARAM_ERROR.getCode(), msgs.toString().substring(1));
    }

    /**
     * 不支持Http请求方法产生异常时的处理
     * 如方法Controller中申明方法需要Post方式提交，而客户端使用了Get方法，
     * 此时产生的异常由本方法统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.debug("Http请求方法错误，本次请求的方法:{}，需要使用的请求方法:{}", e.getMethod(), e.getSupportedMethods());
        return new Response(StatusCode.REQUEST_METHOE_ERROR);
    }

    /**
     * 正常业务异常的处理
     * 业务异常信息可通过msg字段反馈给用户
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response handlerBusinessException(BusinessException e) {
        log.error("BusinessException, errorCode:[{}], errorMessage:[{}]", e.getCode(), e.getMsg());
        return new Response(e.getCode(), e.getMsg());
    }

    /**
     * 没有操作系统相关资源权限时产生的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response UnauthorizedException(UnauthorizedException e) {
        log.debug("shiro权限异常：{}", e.getMessage());
        return new Response(StatusCode.USER_PERMISSION_REQUIRED);
    }

    /**
     * 除业务异常外的其他所有异常的处理
     * 非业务异常信息通常不需要给用户看，因为普通用户也看不懂，所以这里统一反馈"系统内部错误"
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Response handlerAllException(Exception e) {
        log.error("", e);
        return new Response(StatusCode.SYSTEM_ERROR,e.getMessage());
    }
}
