package com.nhnmart.servicecenter.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {

    // 답변할 문의의 ID를 받기 위한 필드
    @NotNull(message = "문의 ID가 누락되었습니다.")
    private Long inquiryId;

    @NotNull(message = "답변 내용은 필수 항목입니다.")
    @Size(min = 1, max = 40000, message = "답변은 1자 이상 40,000자 이하로 입력해야 합니다.")
    private String content;

}
