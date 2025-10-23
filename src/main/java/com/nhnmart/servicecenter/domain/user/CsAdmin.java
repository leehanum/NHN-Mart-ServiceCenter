package com.nhnmart.servicecenter.domain.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CsAdmin extends User{

    public CsAdmin(){
        this.setType(UserType.CS_ADMIN);
    }
}
