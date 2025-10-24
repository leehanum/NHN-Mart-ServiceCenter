package com.nhnmart.servicecenter.exception;

import com.nhnmart.servicecenter.domain.user.User;
import com.nhnmart.servicecenter.respository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

public class AdminCheckInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    public AdminCheckInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("userId");

        User user = userRepository.getUser(userId);


        if (Objects.isNull(user) || !user.isCsAdmin()) {
            throw new AdminAccessDeniedException();
        }

        return true;
    }
}
