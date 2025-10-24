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
            // 403 Forbidden 응답을 주거나, 권한이 없음을 알리는 페이지로 리다이렉션
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 필요합니다.");
            // 또는 response.sendRedirect("/cs/access-denied");
            return false;
        }

        return true;
    }
}
