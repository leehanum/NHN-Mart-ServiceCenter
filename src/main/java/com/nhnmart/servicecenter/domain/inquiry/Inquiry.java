package com.nhnmart.servicecenter.domain.inquiry;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

// 문의 테이블
@Getter
@Setter
public class Inquiry {

    // 문의 공통 정보
    private Long id; // 문의 ID (Repository에서 할당)
    private String title; // 제목 (2~200자)
    private InquiryCategory category; // 분류
    private String content; // 본문 (0~40,000자)
    private String customerId; // 작성자 ID
    private LocalDateTime createdAt; // 문의 작성 일시
    private boolean isAnswered; // 답변 여부

    // 첨부 파일 정보
    private List<String> attachmentFilePaths;

    // 답변 정보
    private String answerContent; // 답변 내용
    private LocalDateTime answeredAt; // 답변 일시
    private String csAdminId; // 답변 CS 담당자 ID

    /**
     * 1. 기본 생성자 (NoArgsConstructor): 객체를 생성한 후 Setter로 값을 설정할 때 사용
     */
    public Inquiry() {
        // Inquiry 객체 생성 시 기본값 설정
        this.createdAt = LocalDateTime.now();
        this.isAnswered = false;
    }

    /**
     * 2. 필수 필드 생성자: 문의글 작성 시 주로 사용되는 필드만 받는 생성자
     */
    public Inquiry(String title, InquiryCategory category, String content, String customerId, List<String> attachmentFilePaths) {
        this(); // 기본 생성자를 호출하여 createdAt과 isAnswered를 초기화
        this.title = title;
        this.category = category;
        this.content = content;
        this.customerId = customerId;
        this.attachmentFilePaths = attachmentFilePaths;
    }

    // 고객 페이지에서 답변이 완료되었는지 확인하는 헬퍼 메서드
    public boolean hasAnswer() {
        return this.isAnswered && this.answerContent != null;
    }
}