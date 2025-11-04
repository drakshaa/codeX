package com.nits.codex.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nits.codex.model.User;
import com.nits.codex.repository.UserRepository;
import com.nits.codex.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;
    
    @Override
    public boolean userSignup(User u) {
        if (userRepo.findByUsername(u.getUsername()) != null) {
            return false;
        }
        userRepo.save(u);
        return true;
    }

    @Override
    public User userLogin(String identifier, String psw) {
    	return userRepo.findByUsernameOrEmailAndPassword(identifier, identifier, psw);
    }
}
