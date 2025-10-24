package com.nhnmart.servicecenter.controller.inquiry;

import com.nhnmart.servicecenter.domain.inquiry.Inquiry;
import com.nhnmart.servicecenter.domain.inquiry.InquiryCategory;
import com.nhnmart.servicecenter.domain.user.User;
import com.nhnmart.servicecenter.domain.user.UserType;
import com.nhnmart.servicecenter.respository.InquiryRepository;
import com.nhnmart.servicecenter.respository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InquiryController.class)
class InquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean private InquiryRepository inquiryRepository;

    private final String CUSTOMER_ID = "lee";

    // -----------------------------------------------------------------
    // 1. GET /cs 테스트 (문의 목록 조회) 커버리지 보강
    // -----------------------------------------------------------------


    @Test
    @DisplayName("2.1. GET /cs/inquiry: 로그인된 상태에서 문의 폼 반환")
    void customerInquiryForm_ShouldReturnFormView() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", CUSTOMER_ID);

        // When & Then
        mockMvc.perform(get("/cs/inquiry")
                        .session(session)) // 세션 주입 (LoginCheckInterceptor 통과)
                .andExpect(status().isOk())
                .andExpect(view().name("/customer/CustomerInquiryForm"));
    }

}