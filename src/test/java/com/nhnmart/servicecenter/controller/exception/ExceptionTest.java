package com.nhnmart.servicecenter.controller.exception;

import com.nhnmart.servicecenter.controller.LoginController;
import com.nhnmart.servicecenter.domain.user.User;
import com.nhnmart.servicecenter.domain.user.UserType;
import com.nhnmart.servicecenter.exception.LoginFailException;
import com.nhnmart.servicecenter.respository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class ExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    private final String CUSTOMER_ID = "lee";
    private final String WRONG_PASSWORD = "wrong_password";
    private final String ID_PARAM_NAME = "id";       // LoginController의 @RequestParam 이름
    private final String PW_PARAM_NAME = "password"; // LoginController의 @RequestParam 이름


    @Test
    @DisplayName("인증 실패 시 (matches=false) LoginFailException 발생 및 500 에러 처리")
    void loginFailExceptionTest_ThrowsException() throws Exception {
        when(userRepository.matches(CUSTOMER_ID, WRONG_PASSWORD)).thenReturn(false);

        mockMvc.perform(post("/cs/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(ID_PARAM_NAME, CUSTOMER_ID)
                        .param(PW_PARAM_NAME, WRONG_PASSWORD))

                .andExpect(status().isInternalServerError())

                .andExpect(view().name("error"))

                .andExpect(model().attribute("exception", instanceOf(LoginFailException.class)))
                .andExpect(request().sessionAttributeDoesNotExist("userId"));
    }

}