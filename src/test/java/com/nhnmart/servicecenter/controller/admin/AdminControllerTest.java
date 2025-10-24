package com.nhnmart.servicecenter.controller.admin;

import com.nhnmart.servicecenter.domain.inquiry.Inquiry;
import com.nhnmart.servicecenter.domain.inquiry.InquiryCategory;
import com.nhnmart.servicecenter.domain.user.User;
import com.nhnmart.servicecenter.domain.user.UserType;
import com.nhnmart.servicecenter.respository.InquiryRepository;
import com.nhnmart.servicecenter.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private InquiryRepository inquiryRepository;
    @MockitoBean
    private UserRepository userRepository;

    private MockHttpSession adminSession;
    private final String ADMIN_ID = "admin";
    private final Long INQUIRY_ID = 1L;
    private Inquiry validInquiry;

    @BeforeEach
    void setup() {
        // 1. 세션 설정
        adminSession = new MockHttpSession();
        adminSession.setAttribute("userId", ADMIN_ID);

        // 2. 인터셉터 통과 Mocking: Admin User 객체 설정
        User adminUser = new User(ADMIN_ID, "pw", "관리자", UserType.CS_ADMIN);
        when(userRepository.getUser(ADMIN_ID)).thenReturn(adminUser);
        when(userRepository.exists(ADMIN_ID)).thenReturn(true);

        // 3. 공통 Inquiry 객체 Mocking
        validInquiry = new Inquiry("제목", InquiryCategory.OTHER, "내용", "user1", List.of());
        validInquiry.setId(INQUIRY_ID);
    }

    @Test
    @DisplayName("1. GET /admin: 미답변 목록 조회 시 CsAdminInquiryList 반환")
    void adminInquiryList_ShouldReturnUnansweredList() throws Exception {
        // Given
        when(inquiryRepository.findUnanswered()).thenReturn(Collections.singletonList(validInquiry));

        // When & Then
        mockMvc.perform(get("/cs/admin")
                        .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/CsAdminInquiryList"))
                .andExpect(model().attributeExists("inquiries"));
    }
    @Test
    @DisplayName("2. GET /answer/{id}: 답변 폼 요청 시 문의 상세 정보 반환")
    void answerForm_ShouldReturnAnswerForm() throws Exception {
        // Given
        // ⭐️ findById가 유효한 Optional을 반환하도록 Mocking
        when(inquiryRepository.findById(INQUIRY_ID)).thenReturn(Optional.of(validInquiry));

        // When & Then
        mockMvc.perform(get("/cs/admin/answer/{answerId}", INQUIRY_ID)
                        .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/CsAdminAnswerForm"))
                .andExpect(model().attribute("inquiry", validInquiry));
    }
    @Test
    @DisplayName("3.1. POST /answer: 답변 성공 시 /cs/admin으로 리다이렉션")
    void answer_Success_ShouldUpdateInquiryAndRedirect() throws Exception {
        // Given
        when(inquiryRepository.findById(INQUIRY_ID)).thenReturn(Optional.of(validInquiry));
        when(inquiryRepository.save(any(Inquiry.class))).thenReturn(INQUIRY_ID);

        // When & Then
        mockMvc.perform(post("/cs/admin/answer")
                        .session(adminSession)
                        .param("inquiryId", String.valueOf(INQUIRY_ID))
                        .param("content", "네, 신속하게 처리해 드리겠습니다."))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));
    }



    @Test
    @DisplayName("4.2. POST /answer: 이미 답변된 문의에 재답변 시도")
    void answer_Failure_AlreadyAnswered() throws Exception {
        // Given
        Inquiry alreadyAnsweredInquiry = new Inquiry("제목", InquiryCategory.OTHER, "내용", "user1", List.of());
        alreadyAnsweredInquiry.setId(INQUIRY_ID);
        alreadyAnsweredInquiry.setAnswered(true); // ⭐️ 답변 완료 상태

        when(inquiryRepository.findById(INQUIRY_ID)).thenReturn(Optional.of(alreadyAnsweredInquiry));

        // When & Then
        // Controller의 if (!inquiry.isAnswered()) 로직을 건너뛰고 정상 리다이렉션
        mockMvc.perform(post("/cs/admin/answer")
                        .session(adminSession)
                        .param("inquiryId", String.valueOf(INQUIRY_ID))
                        .param("content", "두 번째 답변 시도"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));
    }
}