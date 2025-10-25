package com.nhnmart.servicecenter.advice;

import com.nhnmart.servicecenter.exception.AdminAccessDeniedException;
import com.nhnmart.servicecenter.exception.LoginFailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import static org.assertj.core.api.Assertions.assertThat;

class WebControllerAdviceTest {

    private WebControllerAdvice advice;
    private Model model;

    @BeforeEach
    void setUp() {
        advice = new WebControllerAdvice();
        model = new BindingAwareModelMap();
    }

    @Test
    @DisplayName("일반 Exception 처리 시 error 뷰 반환")
    void handleException_ShouldReturnErrorView() {
        Exception exception = new RuntimeException("일반 에러 발생");

        String viewName = advice.handleException(exception, model);

        assertThat(viewName).isEqualTo("error");
        assertThat(model.getAttribute("exception")).isEqualTo(exception);
    }

    @Test
    @DisplayName("AdminAccessDeniedException 처리 시 error 뷰 반환")
    void handleAccessDeniedException_ShouldReturnErrorView() {
        AdminAccessDeniedException exception = new AdminAccessDeniedException();

        String viewName = advice.handleAccessDeniedException(exception, model);

        assertThat(viewName).isEqualTo("error");
        assertThat(model.getAttribute("exception")).isEqualTo(exception);
    }

    @Test
    @DisplayName("LoginFailException 처리 시 error 뷰 반환")
    void handleLoginFailException_ShouldReturnErrorView() {
        LoginFailException exception = new LoginFailException();

        String viewName = advice.handleLoginFailException(exception, model);

        assertThat(viewName).isEqualTo("error");
        assertThat(model.getAttribute("exception")).isEqualTo(exception);
    }

    @Test
    @DisplayName("LoginFailException 커스텀 메시지 처리")
    void handleLoginFailException_WithCustomMessage() {
        LoginFailException exception = new LoginFailException("계정이 잠겼습니다.");

        String viewName = advice.handleLoginFailException(exception, model);

        assertThat(viewName).isEqualTo("error");
        LoginFailException modelException = (LoginFailException) model.getAttribute("exception");
        assertThat(modelException.getMessage()).isEqualTo("계정이 잠겼습니다.");
    }
}