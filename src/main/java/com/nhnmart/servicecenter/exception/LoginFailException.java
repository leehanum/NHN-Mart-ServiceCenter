package com.nhnmart.servicecenter.exception;

public class LoginFailException extends RuntimeException {
    public LoginFailException() {
        this("로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.");
    }

    public LoginFailException(String message) {
        super(message);
    }
}
