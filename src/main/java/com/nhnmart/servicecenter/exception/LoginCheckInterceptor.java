package com.nhnmart.servicecenter.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if(Objects.isNull(session) || Objects.isNull(session.getAttribute("userId"))){
            response.sendRedirect("/cs/login");
            return false;
        }
        String userId =  (String)session.getAttribute("userId");
        System.out.println(userId);


        return true;
    }
}
