package com.nhnmart.servicecenter.exception;

import com.nhnmart.servicecenter.domain.user.User;
import com.nhnmart.servicecenter.domain.user.UserType;
import com.nhnmart.servicecenter.respository.UserRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCheckInterceptorTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private AdminCheckInterceptor interceptor;

    private final String adminId = "admin";
    private final String customerId = "customer";

    @BeforeEach
    void setUp() {
        interceptor = new AdminCheckInterceptor(userRepository);
    }

    @Test
    @DisplayName("관리자 권한이 있으면 true 반환")
    void preHandle_AdminUser_ShouldReturnTrue() {
        User adminUser = new User(adminId, "1234", "관리자", UserType.CS_ADMIN);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(adminId);
        when(userRepository.getUser(adminId)).thenReturn(adminUser);

        boolean result;
        try {
            result = interceptor.preHandle(request, response, new Object());
        } catch (Exception e) {
            throw new AssertionError("예외가 발생하지 않아야 합니다", e);
        }

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("일반 사용자가 접근하면 AdminAccessDeniedException 발생")
    void preHandle_CustomerUser_ShouldThrowException() {
        User customerUser = new User(customerId, "1234", "고객", UserType.CUSTOMER);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(customerId);
        when(userRepository.getUser(customerId)).thenReturn(customerUser);

        assertThatThrownBy(() -> {
            try {
                interceptor.preHandle(request, response, new Object());
            } catch (AdminAccessDeniedException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })
                .isInstanceOf(AdminAccessDeniedException.class)
                .hasMessage("관리자 권한이 필요합니다.");
    }

    @Test
    @DisplayName("사용자 정보가 null이면 AdminAccessDeniedException 발생")
    void preHandle_NullUser_ShouldThrowException() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("unknown");
        when(userRepository.getUser("unknown")).thenReturn(null);

        assertThatThrownBy(() -> {
            try {
                interceptor.preHandle(request, response, new Object());
            } catch (AdminAccessDeniedException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        })
                .isInstanceOf(AdminAccessDeniedException.class)
                .hasMessage("관리자 권한이 필요합니다.");
    }
}