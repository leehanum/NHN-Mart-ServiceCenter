package com.nhnmart.servicecenter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LoginFailExceptionTest {

    @Test
    @DisplayName("LoginFailException 기본 생성자 메시지 확인")
    void testDefaultConstructorMessage() {
        LoginFailException exception = new LoginFailException();
        assertThat(exception.getMessage()).isEqualTo("로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("LoginFailException 커스텀 메시지 생성자 확인")
    void testCustomMessageConstructor() {
        String customMessage = "계정이 잠겼습니다.";
        LoginFailException exception = new LoginFailException(customMessage);
        assertThat(exception.getMessage()).isEqualTo(customMessage);
    }

    @Test
    @DisplayName("LoginFailException이 RuntimeException을 상속하는지 확인")
    void testExceptionType() {
        LoginFailException exception = new LoginFailException();
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}