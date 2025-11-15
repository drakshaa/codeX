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
        // Find user by username or email
        User user = userRepo.findByUsernameOrEmail(identifier, identifier);
        
        if (user == null) {
            return null; // User not found
        }

  
        if (user.getPassword().equals(psw)) {
            return user; 
        } else {
            return null; 
        }
    }

    @Override
    public User registerNewUser(String fname, String lname, String username, String email, String password) { // ⭐ FIX: Renamed parameters to match fields
        
        if (userRepo.findByUsername(username) != null || userRepo.findByEmail(email) != null) {
             return null;
        }

    
        User user = new User();
        user.setFname(fname);
        user.setLname(lname); 
        user.setUsername(username);
        user.setEmail(email);

        user.setPassword(password);

        return userRepo.save(user);
    }
}