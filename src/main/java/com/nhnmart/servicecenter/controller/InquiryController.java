package com.nhnmart.servicecenter.controller;

import com.nhnmart.servicecenter.domain.inquiry.Inquiry;
import com.nhnmart.servicecenter.domain.inquiry.InquiryCategory;
import com.nhnmart.servicecenter.domain.inquiry.InquiryForm;
import com.nhnmart.servicecenter.domain.user.User;
import com.nhnmart.servicecenter.exception.ValidationFailedException;
import com.nhnmart.servicecenter.respository.InquiryRepository;
import com.nhnmart.servicecenter.respository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Slf4j
@RequestMapping("/cs")
@Controller
public class InquiryController {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;
    private static final String UPLOAD_DIR = "/Users/leehanum/Downloads/";


    @Autowired
    public InquiryController(InquiryRepository inquiryRepository, UserRepository userRepository) {
        this.inquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String customerInquiryList(@RequestParam(name = "category", required = false) String categoryName,
                                      HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        String customerId = (String) session.getAttribute("userId");

        User customer = userRepository.getUser(customerId);
        if(Objects.isNull(customer)){
            return "redirect:/cs/logout";
        }
        model.addAttribute("user", customer); // session으로 user 가져와서 model에 저장

        List<Inquiry> inquiries;
        InquiryCategory currentCategory = null;
        if(categoryName != null && !categoryName.isEmpty()){
            try{
                // 분류 검색 요청
                log.info("분류 검색 요청");
                currentCategory = InquiryCategory.valueOf(categoryName.toUpperCase());
                inquiries = inquiryRepository.findByCustomerIdAndCategory(customerId, currentCategory);
                model.addAttribute("currentCategory", currentCategory.name());
            } catch (IllegalArgumentException e){
                inquiries = inquiryRepository.findByCustomerId(customerId);
            }
        } else{
            // 전체 목록 조회 (기본 동작)
            inquiries = inquiryRepository.findByCustomerId(customerId);
        }
        model.addAttribute("inquiries",inquiries);
        model.addAttribute("categories", InquiryCategory.values());

//        model.addAttribute("categories",)
        return "CustomerInquiryList";
    }

    @GetMapping("/inquiry")
    public String customerInquiryForm(){
        return "CostomerInquiryForm";
    }

    @PostMapping("/inquiry")
    public String customerInquiry(@RequestParam("attachments") List<MultipartFile> files,
                                  @Valid @ModelAttribute InquiryForm form, BindingResult bindingResult,
                                  Model model, HttpServletRequest request) throws IOException {
        // Validation을 적용하기 위해 InquiryForm으로 임시로 요청을 받아 @ModelAttribute해서 객체로 받음!
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("userId");

        if(userId == null){
            return "redirect:/cs/logout";
        }

        List<String> filePath = new ArrayList<>(); // 서버 저장 경로(Inquiry 객체에 사용)
        List<String> uploadedFileNames = new ArrayList<>(); // 업로드된 파일 이름 (Model에 사용)
        long totalSize = 0; // 총 파일 크기 (Model에 사용)

        for (MultipartFile file : files) {
            if(!file.isEmpty()){
                // 1. 파일 저장 경로 생성
                String originalFilename = file.getOriginalFilename();
                String savePath = UPLOAD_DIR + file.getOriginalFilename();
                // 2.파일 저장
                file.transferTo(Paths.get(savePath));
                // 3. 정보 수집
                filePath.add(savePath);
                uploadedFileNames.add(originalFilename); // view에 보여줄 이름 리스트
                totalSize += file.getSize(); // 전체 크기 합산
            }

        }

        Inquiry inquiry = new Inquiry(form.getTitle(), form.getCategory(), form.getContent(), userId, filePath);
        inquiryRepository.save(inquiry);

        model.addAttribute("uploadedCount", uploadedFileNames.size()); // 업로드된 파일 개수
        model.addAttribute("uploadedFileNames", uploadedFileNames); // 업로드된 파일 이름 리스트
        model.addAttribute("totalSize", totalSize);

        return "redirect:/cs";
    }
}
