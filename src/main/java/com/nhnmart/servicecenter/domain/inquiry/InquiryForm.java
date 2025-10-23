package com.nhnmart.servicecenter.domain.inquiry;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryForm {

    private InquiryCategory category;

    @NotNull(message = "제목은 필수 항목입니다.")
    @Size(min = 2, max = 200, message = "제목은 2자 이상 200자 이하로 입력해야 합니다.")
    private String title;

    @Size(max = 40000, message = "본문은 40,000자를 초과할 수 없습니다.")
    private String content;
}