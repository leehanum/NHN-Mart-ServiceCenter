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

    private final String ADMIN_ID = "admin";
    private final String CUSTOMER_ID = "customer";

    @BeforeEach
    void setUp() {
        interceptor = new AdminCheckInterceptor(userRepository);
    }

    @Test
    @DisplayName("관리자 권한이 있으면 true 반환")
    void preHandle_AdminUser_ShouldReturnTrue() throws Exception {
        User adminUser = new User(ADMIN_ID, "1234", "관리자", UserType.CS_ADMIN);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(ADMIN_ID);
        when(userRepository.getUser(ADMIN_ID)).thenReturn(adminUser);

        boolean result = interceptor.preHandle(request, response, new Object());

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("일반 사용자가 접근하면 AdminAccessDeniedException 발생")
    void preHandle_CustomerUser_ShouldThrowException() {
        User customerUser = new User(CUSTOMER_ID, "1234", "고객", UserType.CUSTOMER);
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(CUSTOMER_ID);
        when(userRepository.getUser(CUSTOMER_ID)).thenReturn(customerUser);

        assertThatThrownBy(() -> interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(AdminAccessDeniedException.class)
                .hasMessage("관리자 권한이 필요합니다.");
    }

    @Test
    @DisplayName("사용자 정보가 null이면 AdminAccessDeniedException 발생")
    void preHandle_NullUser_ShouldThrowException() {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("unknown");
        when(userRepository.getUser("unknown")).thenReturn(null);

        assertThatThrownBy(() -> interceptor.preHandle(request, response, new Object()))
                .isInstanceOf(AdminAccessDeniedException.class)
                .hasMessage("관리자 권한이 필요합니다.");
    }
}
