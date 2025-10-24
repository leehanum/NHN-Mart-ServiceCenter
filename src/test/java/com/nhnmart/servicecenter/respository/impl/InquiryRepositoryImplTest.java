package com.nhnmart.servicecenter.respository.impl;

import com.nhnmart.servicecenter.domain.inquiry.Inquiry;
import com.nhnmart.servicecenter.domain.inquiry.InquiryCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


class InquiryRepositoryImplTest {

    private InquiryRepositoryImpl inquiryRepository;

    private final String CUSTOMER_ID = "test_user";
    private final String OTHER_ID = "other_user";

    @BeforeEach
    void setUp(){
        inquiryRepository = new InquiryRepositoryImpl();
        inquiryRepository.clear();
    }

    @Test
    @DisplayName("1. 새로운 문의를 저장하고 ID가 할당되는지 확인")
    void save() {
        // given
        Inquiry newInquiry = new Inquiry("제목", InquiryCategory.OTHER, CUSTOMER_ID, OTHER_ID, List.of());

        // when
        Long savedId = inquiryRepository.save(newInquiry);

        // then
        assertThat(savedId).isNotNull(); // ID가 할당되었는지 확인
        System.out.println(savedId);
        assertThat(newInquiry.getId()).isEqualTo(1L); // 저장소의 시퀀스 로직(1부터 시작)을 확인
        Optional<Inquiry> found = inquiryRepository.findById(savedId);
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("제목");
    }

    @Test
    @DisplayName("2. ID로 문의를 조회: 성공")
    void findById() {
        //given
        Inquiry newInquiry = new Inquiry("제목", InquiryCategory.OTHER, CUSTOMER_ID, OTHER_ID, List.of());
        Long savedId = inquiryRepository.save(newInquiry);

        // when
        Optional<Inquiry> found = inquiryRepository.findById(savedId);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedId);
    }

    @Test
    @DisplayName("3. ID로 문의를 조회: 실패 (존재하지 않는 ID)")
    void findById_NonExistingId_ShouldReturnEmpty() {
        Optional<Inquiry> inquiry = inquiryRepository.findById(99L);

        assertThat(inquiry).isEmpty();


    }

    @Test
    @DisplayName("4. 고객 ID로 문의 목록 조회: 최신순 정렬 확인")
    void findByCustomerId_ShouldReturnListSortedByLatest() {
        // Given
        Inquiry q1_old = new Inquiry("오래된 문의", InquiryCategory.COMPLAINT, "내용", CUSTOMER_ID, List.of());
        q1_old.setCreatedAt(LocalDateTime.now().minusDays(2));
        inquiryRepository.save(q1_old);

        Inquiry q2_new = new Inquiry("최신 문의", InquiryCategory.COMPLAINT, "내용", CUSTOMER_ID, List.of());
        q2_new.setCreatedAt(LocalDateTime.now().minusDays(1));
        inquiryRepository.save(q2_new);

        inquiryRepository.save(new Inquiry("다른 유저 문의", InquiryCategory.OTHER, "내용", OTHER_ID, List.of()));

        // When
        List<Inquiry> inquiries = inquiryRepository.findByCustomerId(CUSTOMER_ID);

        // Then
        assertThat(inquiries).hasSize(2);
        // 정렬 확인: 최신(q2_new)이 상단에 위치해야 합니다. (reversed() 때문)
        assertThat(inquiries.get(0).getTitle()).isEqualTo("최신 문의");
        assertThat(inquiries.get(1).getTitle()).isEqualTo("오래된 문의");
    }

    @Test
    @DisplayName("5. 미답변 목록 조회: 답변 안된 문의만, 오래된 순 정렬 확인")
    void findUnanswered_ShouldReturnUnansweredOnlySortedByOldest() {
        Inquiry q1_old_unanswered = new Inquiry("오래된 미답변", InquiryCategory.COMPLAINT, "내용", CUSTOMER_ID, List.of());
        q1_old_unanswered.setCreatedAt(LocalDateTime.now().minusDays(3));
        inquiryRepository.save(q1_old_unanswered);

        Inquiry q2_answered = new Inquiry("답변 완료", InquiryCategory.COMPLAINT, "내용", CUSTOMER_ID, List.of());
        q2_answered.setAnswered(true);
        inquiryRepository.save(q2_answered);

        Inquiry q3_new_unanswered = new Inquiry("최신 미답변", InquiryCategory.COMPLAINT, "내용", CUSTOMER_ID, List.of());
        q3_new_unanswered.setCreatedAt(LocalDateTime.now().minusDays(1));
        inquiryRepository.save(q3_new_unanswered);

        // When
        List<Inquiry> unanswered = inquiryRepository.findUnanswered();

        // Then
        assertThat(unanswered).hasSize(2);
        // 정렬 확인: 오래된 순(오름차순)으로 정렬되어야 합니다.
        assertThat(unanswered.get(0).getTitle()).isEqualTo("오래된 미답변");
        assertThat(unanswered.get(1).getTitle()).isEqualTo("최신 미답변");
    }
}