package com.nhnmart.servicecenter.respository;

import com.nhnmart.servicecenter.domain.inquiry.Inquiry;
import com.nhnmart.servicecenter.domain.inquiry.InquiryCategory;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository {

    /**
     * 새로운 문의를 저장하거나 기존 문의를 업데이트합니다.
     * 저장 시 Inquiry 객체의 ID를 반환해야 합니다.
     * @param inquiry 저장할 문의 객체
     * @return 저장된 문의의 고유 ID
     */
    Long save(Inquiry inquiry);

    /**
     * ID를 사용하여 특정 문의를 조회합니다.
     * @param id 문의 고유 ID
     * @return 문의 객체를 포함하는 Optional
     */
    Optional<Inquiry> findById(Long id);

    /**
     * 특정 고객 ID(작성자)가 작성한 모든 문의 목록을 최신순으로 조회합니다.
     * @param customerId 고객 ID
     * @return 해당 고객의 문의 목록 (없으면 빈 리스트)
     */
    List<Inquiry> findByCustomerId(String customerId);

    /**
     * 아직 답변이 달리지 않은 모든 문의 목록을 작성일시 오름차순(오래된 순)으로 조회합니다.
     * CS 담당자 페이지에서 사용됩니다.
     * @return 미답변 문의 목록
     */
    List<Inquiry> findUnanswered();

    /**
     * 고객의 문의 목록을 분류 기준으로 검색합니다. (고객의 [검색] 기능용)
     * @param customerId 고객 ID
     * @param category 문의 분류
     * @return 조건에 맞는 문의 목록
     */
    List<Inquiry> findByCustomerIdAndCategory(String customerId, InquiryCategory category);

}
