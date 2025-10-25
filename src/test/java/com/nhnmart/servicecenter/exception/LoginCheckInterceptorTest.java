package com.nhnmart.servicecenter.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginCheckInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private LoginCheckInterceptor interceptor;

    @BeforeEach
    void setUp() {
        interceptor = new LoginCheckInterceptor();
    }

    @Test
    @DisplayName("세션에 userId가 있으면 true 반환")
    void preHandle_WithValidSession_ShouldReturnTrue() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("testUser");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("세션이 없으면 /cs/login으로 리다이렉트하고 false 반환")
    void preHandle_WithoutSession_ShouldRedirectToLogin() throws Exception {
        when(request.getSession(false)).thenReturn(null);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isFalse();
        verify(response).sendRedirect("/cs/login");
    }

    @Test
    @DisplayName("세션은 있지만 userId가 없으면 /cs/login으로 리다이렉트하고 false 반환")
    void preHandle_WithSessionButNoUserId_ShouldRedirectToLogin() throws Exception {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(null);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isFalse();
        verify(response).sendRedirect("/cs/login");
    }
}
