package com.inview.rentserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 自定义拦截器，拦截除/login请求以外的所有请求
 */
//@Configuration
@RequiredArgsConstructor
public class WebConfig extends WebMvcConfigurationSupport {
    private final TokenFilter tokenFilter;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.tokenFilter).addPathPatterns("/**").excludePathPatterns("/login/**");
        super.addInterceptors(registry);
    }
}
