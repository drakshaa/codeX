package com.nits.codex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nits.codex.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByUsernameOrEmail(String username, String email);

	User findByUsername(String username);
    User findByEmail(String email);
}