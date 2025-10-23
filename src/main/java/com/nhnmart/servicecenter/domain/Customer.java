package com.nhnmart.servicecenter.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer extends User{
    // 고객 구분
    public Customer(){
        this.setType(UserType.CUSTOMER);
    }
}
