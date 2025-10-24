package com.nhnmart.servicecenter.controller.admin;


import com.nhnmart.servicecenter.domain.inquiry.Inquiry;
import com.nhnmart.servicecenter.respository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/cs/admin")
public class AdminController {
    private final InquiryRepository inquiryRepository;

    @Autowired
    public AdminController(InquiryRepository inquiryRepository){
        this.inquiryRepository = inquiryRepository;
    }

    @GetMapping
    public String adminInQuiryList(Model model){
        List<Inquiry> unanswered = inquiryRepository.findUnanswered();// 답변이 달리지 않은 문의 목록 가져오기
        model.addAttribute("inquiries",unanswered);
        return "/admin/CsAdminInquiryList";
    }

    @GetMapping("/answer/{answerId}")
    public String answerForm(@PathVariable(name = "answerId") Long id, Model model){
        Optional<Inquiry> inquiryOptional = inquiryRepository.findById(id);
        Inquiry inquiry = inquiryOptional.get();
        model.addAttribute("inquiry",inquiry);

        return "/admin/CsAdminAnswerForm";
    }

    @PostMapping("/answer")
    public String answer(@RequestParam("content") String content,
                         @PathVariable("inquiryId") Long inquiryId){

        return "";
    }



}
