package com.nhnmart.servicecenter.respository;

import com.nhnmart.servicecenter.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String id);
    List<User> findAll();
    User save(User user);
}
