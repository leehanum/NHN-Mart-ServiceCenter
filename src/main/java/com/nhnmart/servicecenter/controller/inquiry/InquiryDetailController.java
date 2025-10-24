package com.nhnmart.servicecenter.controller.inquiry;

import com.nhnmart.servicecenter.domain.inquiry.Inquiry;
import com.nhnmart.servicecenter.respository.InquiryRepository;
import com.nhnmart.servicecenter.respository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
@RequestMapping("/cs/inquiry")
@Controller
public class InquiryDetailController {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    public InquiryDetailController(InquiryRepository inquiryRepository, UserRepository userRepository) {
        this.inquiryRepository = inquiryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        Optional<Inquiry> OptionalInquiry = inquiryRepository.findById(id);
        Inquiry inquiry = OptionalInquiry.get();
        model.addAttribute("inquiry",inquiry);
        return "/customer/CustomerInquiryDetail";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filePath") String filePath) throws IOException{
        if(!filePath.startsWith(uploadDir)){
            log.error("파일 다운로드 보안 위반 시도: {}", filePath);
            return ResponseEntity.status(403).build();
        }
        // 파일 Resource 로드
        Path file = Paths.get(filePath);
        Resource resource = new UrlResource(file.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String originalFileName = file.getFileName().toString();
        String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");

        // 5. HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        // attachment: 브라우저에게 다운로드하도록 지시
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"");
        // 파일의 MIME 타입 설정 (application/octet-stream은 일반적인 이진 파일로 처리)
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        // 파일 크기 추가
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()));

        // 6. 응답 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
