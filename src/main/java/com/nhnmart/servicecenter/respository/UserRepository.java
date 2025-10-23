package com.nhnmart.servicecenter.respository;

import com.nhnmart.servicecenter.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);

    // (선택) 데이터 초기화를 위해 모든 User를 조회하는 메서드
    // List<User> findAll();

    // (선택) 사용자를 저장하는 메서드
    // User save(User user);
}
//