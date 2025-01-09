package com.app.christmas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.christmas.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}