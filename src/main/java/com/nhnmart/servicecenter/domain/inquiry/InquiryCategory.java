package com.nhnmart.servicecenter.domain.inquiry;

public enum InquiryCategory {
    COMPLAINT("불만 접수"),
    SUGGESTION("제안"),
    REFUND_EXCHANGE("환불/교환"),
    PRAISE("칭찬해요"),
    OTHER("기타 문의");

    private final String description;

    InquiryCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}