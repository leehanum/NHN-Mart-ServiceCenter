package com.nhnmart.servicecenter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class LoginFailExceptionTest {

    @Test
    @DisplayName("LoginFailException 메시지 확인")
    void testExceptionMessage(){

        // given & when
        LoginFailException e = new LoginFailException();

        // then
        assertThat(e.getMessage()).isEqualTo("로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.");

    }

    @Test
    @DisplayName("LoginFailException이 RuntimeException을 상속하는지 확인")
    void testExceptionType() {
        LoginFailException exception = new LoginFailException();
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }


}
