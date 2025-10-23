package com.nhnmart.servicecenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/cs")
@Controller
public class CustomerLoginController {



    @GetMapping("/login")
    public String login(){
        return "CustomerLoginForm";
    }


    @PostMapping("/login")
    public String doLogin(){
        return "";
    }
}
