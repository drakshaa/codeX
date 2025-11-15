package com.nits.codex.controller;

import com.nits.codex.model.User;
import com.nits.codex.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // SHOW LOGIN PAGE
    @GetMapping("/login")
    public String showLogin() {
        return "LoginForm";
    }

    // PROCESS LOGIN
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("identifier") String identifier,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        User user = userService.userLogin(identifier, password);

        if (user == null) {
            model.addAttribute("message", "Invalid username/email or password!");
            return "LoginForm";
        }

        session.setAttribute("activeuser", user);

        return "redirect:/";
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    
    @GetMapping("/signup")
    public String showSignupForm() {
        return "SignupForm";
    }

 
    @PostMapping("/signup")
    public String handleSignup(
            @RequestParam("fname") String firstName,
            @RequestParam("lname") String lastName,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model) {
        
       
        User newUser = userService.registerNewUser(
            firstName, 
            lastName, 
            username, 
            email, 
            password
        ); 

        if (newUser == null) {
          
            model.addAttribute("error", "Signup failed. Username or email may already be in use.");
            return "SignupForm";
        }

      
        model.addAttribute("message", "Account created successfully! Please log in.");
        return "redirect:/login";
    }
}