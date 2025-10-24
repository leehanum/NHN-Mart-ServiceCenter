package com.nhnmart.servicecenter.controller;

import com.nhnmart.servicecenter.domain.user.User;
import com.nhnmart.servicecenter.domain.user.UserType;
import com.nhnmart.servicecenter.respository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class CustomerLoginControllerTest {


    @Autowired
    private MockMvc mockMvc;

    // ⭐️ 컨트롤러가 의존하는 UserRepository 인터페이스를 Mocking합니다.
    @MockitoBean
    private UserRepository userRepository;

    @Test // 직접 작성함.
    @DisplayName("GET /cs/login test!!")
    void loginTest() throws Exception {
        // given
        String userId = "lee";
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId",userId);

        when(userRepository.exists(userId)).thenReturn(true);
        when(userRepository.getUser(userId)).thenReturn(new User(userId, "1234", "이한음", UserType.CUSTOMER));

        //when & then
        mockMvc.perform(get("/cs")
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("1. GET /cs/login: 세션이 존재하고 유효하면 /cs로 리다이렉션")
    void loginTest_AlreadyLoggedIn_ShouldRedirectToCs() throws Exception {
        // Given (준비)
        String userId = "lee";

        // 1. Mock 세션 생성 및 로그인 정보 주입
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", userId);

        // 2. Controller 로직 Mocking (login 메소드 로직을 따라갑니다.)
        //    - user.exists(userId)가 호출되면 -> true 반환
        when(userRepository.exists(userId)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/cs/login") // ⭐️ 엔드포인트 수정
                        .session(session)) // ⭐️ 세션 주입
                .andExpect(status().is3xxRedirection()) // 302 리다이렉션 확인
                .andExpect(redirectedUrl("/cs")); // 고객 메인 페이지로 리다이렉션 확인
    }

    @Test
    @DisplayName("2. GET /cs/login: 세션이 없으면 LoginForm 반환")
    void 세션없으면LoginForm반환() throws Exception {

        mockMvc.perform(get("/cs/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("LoginForm"));
    }


}