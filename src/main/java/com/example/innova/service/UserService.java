package com.example.innova.service;

import com.example.innova.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    User saveUser(User user);

    void deleteUser(Long id);

    boolean authenticate(String email, String password);

    User getUserOrThrow(Long id);

    void updateUserBalance(Long id);

    Long getTotalAmountForUser(Long userId);
}
