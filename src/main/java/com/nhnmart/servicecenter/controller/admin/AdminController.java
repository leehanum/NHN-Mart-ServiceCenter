package com.nhnmart.servicecenter.controller.admin;


import com.nhnmart.servicecenter.domain.AnswerForm;
import com.nhnmart.servicecenter.domain.inquiry.Inquiry;
import com.nhnmart.servicecenter.exception.ValidationFailedException;
import com.nhnmart.servicecenter.respository.InquiryRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public String answer(@Valid AnswerForm answerForm, BindingResult bindingResult,
                         HttpSession session){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }
        String csAdminId = (String) session.getAttribute("userId");
        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(answerForm.getInquiryId());

        if(optionalInquiry.isEmpty()){
            throw new RuntimeException("해당 문의를 찾을 수 없습니다.");
        }

        Inquiry inquiry = optionalInquiry.get();

        if(!inquiry.isAnswered()){
            inquiry.setAnswerContent(answerForm.getContent());
            inquiry.setAnswered(true);
            inquiry.setAnsweredAt(LocalDateTime.now());
            inquiry.setCsAdminId(csAdminId);

            inquiryRepository.save(inquiry);
        }

        return "redirect:/cs/admin";
    }


}
