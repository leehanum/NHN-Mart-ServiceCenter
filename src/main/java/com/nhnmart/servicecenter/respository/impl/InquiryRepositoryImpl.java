package com.nhnmart.servicecenter.respository.impl;

import com.nhnmart.servicecenter.domain.inquiry.Inquiry;
import com.nhnmart.servicecenter.domain.inquiry.InquiryCategory;
import com.nhnmart.servicecenter.respository.InquiryRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InquiryRepositoryImpl implements InquiryRepository {
    private final Map<Long, Inquiry> inquiryMap = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L); // 문의 ID를 안전하게 생성하는 카운터

    public InquiryRepositoryImpl() {
        final String customerLee = "lee";
        final String customerHong = "hong";
        final String admin = "admin"; // CS 담당자 ID (CS Admin 계정 생성 필요)

        Inquiry q1 = new Inquiry("배송 지연 문의", InquiryCategory.COMPLAINT,"주문한 상품이 예정보다 늦어집니다.",
                customerLee,List.of());
        this.save(q1);
    }

    @Override
    public Long save(Inquiry inquiry) {
        if(inquiry.getId() == null){
            // 새로 저장하는 경우
            Long newId = sequence.incrementAndGet();
            inquiry.setId(newId);
            inquiryMap.put(newId,inquiry);
            return newId;
        } else{
            // 기존 문의 업데이트
            inquiryMap.put(inquiry.getId(), inquiry);
            return inquiry.getId();
        }
    }

    /**
     * ID를 사용하여 특정 문의를 조회합니다.
     */
    @Override
    public Optional<Inquiry> findById(Long id) {
        return Optional.ofNullable(inquiryMap.get(id));
    }

    /**
     * 특정 고객 ID가 작성한 모든 문의 목록을 최신순으로 조회합니다. (고객 메인용)
     */
    @Override
    public List<Inquiry> findByCustomerId(String customerId) {
        return inquiryMap.values().stream()
                .filter(inquiry -> inquiry.getCustomerId().equals(customerId))
                // 작성일시(createdAt) 기준 내림차순 정렬 (가장 최근에 문의한 글이 가장 상단에 위치)
                .sorted(Comparator.comparing(Inquiry::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 아직 답변이 달리지 않은 모든 문의 목록을 작성일시 오름차순(오래된 순)으로 조회합니다. (CS 관리자용)
     */
    @Override
    public List<Inquiry> findUnanswered() {
        return inquiryMap.values().stream()
                .filter(inquiry -> !inquiry.isAnswered())
                // 작성일시(createdAt) 기준 오름차순 정렬 (오래된 문의가 먼저 처리되도록)
                .sorted(Comparator.comparing(Inquiry::getCreatedAt))
                .collect(Collectors.toList());
    }

    /**
     * 고객의 문의 목록을 분류 기준으로 검색합니다.
     */
    @Override
    public List<Inquiry> findByCustomerIdAndCategory(String customerId, InquiryCategory category) {
        return inquiryMap.values().stream()
                .filter(inquiry -> inquiry.getCustomerId().equals(customerId) && inquiry.getCategory() == category)
                .sorted(Comparator.comparing(Inquiry::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    // 테스트 및 초기화를 위한 메서드
    public void clear() {
        inquiryMap.clear();
        sequence.set(0L);
    }
}
