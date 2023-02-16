package com.design.bs.core.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * shiro框架 自定义session获取方式；
 * 可自定义session获取规则，这里采用ajax请求头X-Token携带sessionId的方式
 **/
@Slf4j
public class MySessionManager extends DefaultWebSessionManager {
    private static final String AUTHORIZATION = "X-Token";
    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";

    public MySessionManager() {
        super();
    }

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        //log.info("==> already to MySessionManager");

        //获取请求头中X-Token中保存的sessionId
        String id = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
        String urlId = (String) getParameter(WebUtils.toHttp(request).getQueryString()).get(AUTHORIZATION);
        if (!StringUtils.isEmpty(id)) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        } else if ( !StringUtils.isEmpty(urlId)){
        	 request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
             request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, urlId);
             request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
             return urlId;
        }else {
        	//否则默认从cookie中获取sessionId
            return super.getSessionId(request, response);
        }
    }
    
    public static Map<String, Object> getParameter(String queryString) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(null!=queryString) {
            if(queryString.contains("=")) {
                String[] keyValues = queryString.split("&");
                for (int i = 0; i < keyValues.length; i++) {
                    String key = keyValues[i].substring(0, keyValues[i].indexOf("="));
                    String value = keyValues[i].substring(keyValues[i].indexOf("=") + 1);
                    map.put(key, value);
                }
            }
        }
        return map;
    }
    
}
