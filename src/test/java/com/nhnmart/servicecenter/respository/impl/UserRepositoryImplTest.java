package com.nhnmart.servicecenter.respository.impl;

import com.nhnmart.servicecenter.domain.user.User;
import com.nhnmart.servicecenter.domain.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserRepositoryImplTest {

    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp(){
        userRepository = new UserRepositoryImpl();
    }

    @Test
    @DisplayName("회원 정보 검증")
    void existsTest(){
        // given

        //when
        User user = new User("user", "1234", "name", UserType.CUSTOMER);
        boolean exists = userRepository.exists("lee"); // userRepository에는 save가 없기때문에 이미 넣어둔 값으로 검증
        //then
        assertTrue(exists);
    }

    @Test
    @DisplayName("아이디 비밀번호 일치 검증")
    void matchesTest(){
        // given
        // userRepository에는 save가 없기때문에 이미 넣어둔 값으로 검증

        // when
        boolean hong = userRepository.matches("hong", "1111");

        //then
        assertEquals(hong,false);
    }

    @Test
    @DisplayName("유저 검증")
    void getUserTest(){

        //given
        //when
        User lee = userRepository.getUser("lee");
        //then
        assertEquals(lee.getId(),"lee");

    }

}