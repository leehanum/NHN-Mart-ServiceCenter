package com.nhnmart.servicecenter.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String id;
    private String password;
    private String name;
    private UserType type; // 사용자 유형 (CUSTOMER, CS_ADMIN)

    public User() {
    }

    public User(String id, String password, String name, UserType type) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.type = type;
    }

    public boolean isCustomer(){
        return this.type == UserType.CUSTOMER;
    }

    public boolean isCsAdmin(){
        return this.type == UserType.CS_ADMIN;
    }
}
