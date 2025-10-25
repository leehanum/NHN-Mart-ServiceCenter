package com.nhnmart.servicecenter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdminAccessDeniedExceptionTest {

    @Test
    @DisplayName("AdminAccessDeniedException 메시지 확인")
    void testExceptionMessage() {
        // given & when
        AdminAccessDeniedException exception = new AdminAccessDeniedException();

        // then
        assertThat(exception.getMessage()).isEqualTo("관리자 권한이 필요합니다.");
    }

    @Test
    @DisplayName("AdminAccessDeniedException이 RuntimeException을 상속하는지 확인")
    void testExceptionType() {
        // given & when
        AdminAccessDeniedException exception = new AdminAccessDeniedException();

        // then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}