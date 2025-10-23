package com.nhnmart.servicecenter.config;

import com.nhnmart.servicecenter.exception.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // LoginCheckInterceptor 적용
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/cs/**")
                .excludePathPatterns("/cs/login","/"); // 로그인과 인덱스 제외하고 적용

    }

}
