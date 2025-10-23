package com.nhnmart.servicecenter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/cs")
@Controller
public class InquiryController {

    @GetMapping("/inquiry")
    public String inquiryForm(){
        return "CostomerInquiryForm";
    }
}
