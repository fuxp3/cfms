package com.design.bs.core.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @description: 跨域请求设置
 * @author: 9527
 * @create: 2019-06-17 16:32
 **/
@Configuration
public class CorsConfig {
    @Bean
    public FilterRegistrationBean corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        corsConfiguration.addAllowedOrigin("*");// 1允许任何域名使用
        corsConfiguration.addAllowedHeader("*");// 2允许任何头
        corsConfiguration.addAllowedMethod("*");// 3允许任何方法（post、get等）
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        FilterRegistrationBean registration = new FilterRegistrationBean(new CorsFilter(source));
        registration.setOrder(0); // 这个顺序很重要，为避免麻烦请设置在最前
        return registration;
    }
}
