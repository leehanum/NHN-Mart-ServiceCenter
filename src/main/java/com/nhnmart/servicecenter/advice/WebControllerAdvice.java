package com.nhnmart.servicecenter.advice;

import com.nhnmart.servicecenter.exception.AdminAccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model){
        model.addAttribute("exception",ex);
        return "error";
    }

    @ExceptionHandler(AdminAccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public String handleAccessDeniedException(AdminAccessDeniedException ex, Model model){
        model.addAttribute("exception",ex);
        return "error";
    }
}
