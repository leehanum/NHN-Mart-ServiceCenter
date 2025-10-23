package com.nhnmart.servicecenter.respository.impl;

import com.nhnmart.servicecenter.domain.User;
import com.nhnmart.servicecenter.domain.UserType;
import com.nhnmart.servicecenter.respository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> userMap = new HashMap<>();

    public UserRepositoryImpl() {
        User lee = new User("lee","1234","이한음", UserType.CUSTOMER);
        User hong = new User("hong","1234","홍길동", UserType.CUSTOMER);
        userMap.put(lee.getId(),lee);
        userMap.put(hong.getId(),hong);
    }


    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public boolean exists(String id) {
        return userMap.containsKey(id);
    }

    @Override
    public boolean matches(String id, String password) {
        return Optional.ofNullable((getUser(id)))
                .map(user -> user.getPassword().equals(password))
                .orElse(false);

    }

    @Override
    public User getUser(String id) {
        User user = userMap.get(id);
        return user;
    }
}
