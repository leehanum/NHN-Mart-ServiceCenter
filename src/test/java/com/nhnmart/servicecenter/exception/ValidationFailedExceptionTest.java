package com.nhnmart.servicecenter.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import static org.assertj.core.api.Assertions.assertThat;

class ValidationFailedExceptionTest {

    @Test
    @DisplayName("ValidationFailedException 단일 에러 메시지 확인")
    void testSingleValidationError() {
        Object target = new Object();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "testObject");
        bindingResult.addError(new FieldError("testObject", "title", "제목은 필수 항목입니다."));

        ValidationFailedException exception = new ValidationFailedException(bindingResult);

        assertThat(exception.getMessage())
                .contains("ObjectName=testObject")
                .contains("Message=제목은 필수 항목입니다.");
    }

    @Test
    @DisplayName("ValidationFailedException 여러 에러 메시지 확인")
    void testMultipleValidationErrors() {
        Object target = new Object();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "inquiryForm");
        bindingResult.addError(new FieldError("inquiryForm", "title", "제목은 필수 항목입니다."));
        bindingResult.addError(new FieldError("inquiryForm", "content", "본문은 40,000자를 초과할 수 없습니다."));

        ValidationFailedException exception = new ValidationFailedException(bindingResult);

        assertThat(exception.getMessage())
                .contains("ObjectName=inquiryForm")
                .contains("Message=제목은 필수 항목입니다.")
                .contains("Message=본문은 40,000자를 초과할 수 없습니다.")
                .contains("|");
    }

    @Test
    @DisplayName("ValidationFailedException이 RuntimeException을 상속하는지 확인")
    void testExceptionType() {
        Object target = new Object();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "testObject");
        bindingResult.addError(new FieldError("testObject", "field", "error"));

        ValidationFailedException exception = new ValidationFailedException(bindingResult);

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}