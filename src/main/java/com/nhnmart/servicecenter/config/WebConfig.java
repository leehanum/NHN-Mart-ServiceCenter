package com.nhnmart.servicecenter.config;

import com.nhnmart.servicecenter.exception.LoginCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
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
    // 첨부파일 업로드를 위한 config 추가
    @Bean
    public MultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }

}
