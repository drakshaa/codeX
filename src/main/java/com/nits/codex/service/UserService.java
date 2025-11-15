package com.nits.codex.service;

import com.nits.codex.model.User;

public interface UserService {
	boolean userSignup(User u);
	User userLogin(String identifier, String password);
	User registerNewUser(String firstName, String lastName, String username, String email, String password);
	
}
