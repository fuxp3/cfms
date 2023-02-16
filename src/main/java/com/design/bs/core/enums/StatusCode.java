package com.design.bs.core.enums;

import com.design.bs.core.utils.PropertiesFileReader;
import com.design.bs.core.exception.ICommonError;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 状态码
 **/
@Slf4j
public enum StatusCode implements ICommonError {

	// Http请求错误相关
	REQUEST_PATH_ERROR("00100"), REQUEST_METHOE_ERROR("00101"), REQUEST_PARAM_ERROR("00102"),

	// 用户认证授权相关
	USER_LOGIN_REQUIRED("00120"), USER_PERMISSION_REQUIRED("00121"), USER_NAME_PASS_ERROR("00122"), USER_ACCOUNT_LOCKED(
			"00123"), USER_LOGIN_FAIL("00124"), USER_CODE_ERROR("00125"),

	/**
	 * opeanApi
	 */
	TOKEN_INVALID("00600"), // 无效的token
	TOKEN_HAS_PERMISSION("00601"), // 拥有访问权限
	TOKEN_NOT_HAS_PERMISSION("00602"), // 无权访问
	TOKEN_AUTH_EXCEPTION("00603"), // 权限认证异常


	OK("0"), SYSTEM_ERROR("1");
	private String code;
	private String msg;

	StatusCode(String code) {
		this.code = code;
		this.msg = PropertiesFileReader.getProperty(code, "no message found by this code:" + code);
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
