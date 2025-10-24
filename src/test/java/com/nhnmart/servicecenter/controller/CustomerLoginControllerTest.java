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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class CustomerLoginControllerTest {


    @Autowired
    private MockMvc mockMvc;

    // ⭐️ 컨트롤러가 의존하는 UserRepository 인터페이스를 Mocking합니다.
    @MockitoBean
    private UserRepository userRepository;

    private final String CUSTOMER_ID = "lee";
    private final String PASSWORD = "1234";
    private final String ADMIN_ID = "admin";

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
    void 세션존재리다이렉션() throws Exception {
        // given
        String userId = "lee";

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", userId);
        when(userRepository.exists(userId)).thenReturn(true);

        // when & then
        mockMvc.perform(get("/cs/login")
                        .session(session))
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
    @Test
    @DisplayName("login post 요청")
    void 포스트요청을해보아요() throws Exception {
        //given
        String userId = "lee";
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId",userId);

        User customerUser = new User(CUSTOMER_ID, PASSWORD, "고객이름", UserType.CUSTOMER);

        when(userRepository.matches(CUSTOMER_ID,PASSWORD)).thenReturn(true);
        when(userRepository.exists(any())).thenReturn(true);
        // when & then
        mockMvc.perform(post("/cs/login")
                .session(session)
                .param("id",CUSTOMER_ID)
                .param("password",PASSWORD))
                .andExpect(status().is3xxRedirection())// 302
                .andExpect(redirectedUrl("/cs"))
                .andExpect(cookie().exists("MYSESSION")) // 쿠키 생성 확인
                .andExpect(request().sessionAttribute("userId", CUSTOMER_ID)); // 세션에 ID 저장 확인
    }

    @Test
    @DisplayName("admin 계정일 때 CsAdminInquiryList로 가는지")
    void csAdminInquriyList테스트() throws Exception {
        //given
        User adminUser = new User(ADMIN_ID, PASSWORD, "관리자", UserType.CS_ADMIN);
        when(userRepository.matches(ADMIN_ID,PASSWORD)).thenReturn(true);
        when(userRepository.getUser(ADMIN_ID)).thenReturn(adminUser);
        mockMvc.perform(post("/cs/login")
                .param("id",ADMIN_ID)
                .param("password",PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"))
                .andExpect(request().sessionAttribute("userId",ADMIN_ID));

    }



}