package com.design.bs.core.handler;

import com.design.bs.core.dto.Response;
import com.design.bs.core.enums.StatusCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局响应处理
 *
 * @description: 统一在controller响应的json串外面包一层响应状态码和状态信息
 * @author: 9527
 * @create: 2019-02-27 17:02
 **/
@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {
	@Override
	public boolean supports(MethodParameter methodParameter, Class clazz) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object returnValue, MethodParameter methodParameter, MediaType mediaType, Class clazz,
			ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
		if (returnValue != null) {
			if (returnValue instanceof Response)
				return returnValue;

			return new Response(StatusCode.OK, returnValue);
		}

		return new Response(StatusCode.OK);
	}

}
