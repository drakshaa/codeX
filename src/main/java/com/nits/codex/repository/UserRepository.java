package com.nits.codex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nits.codex.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByUsernameOrEmailAndPassword(String username, String email, String password);
	User findByUsername(String username);
}