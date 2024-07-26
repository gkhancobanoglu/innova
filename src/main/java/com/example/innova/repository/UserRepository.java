package com.example.innova.repository;

import com.example.innova.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByMail(String email);
}
