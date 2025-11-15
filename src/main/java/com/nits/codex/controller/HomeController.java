package com.nits.codex.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // HOME PAGE
    @GetMapping("/")
    public String home() {
        return "Home";      // templates/Home.html
    }

    // JOIN US LOGIC
    @GetMapping("/join-us")
    public String joinUs(HttpSession session) {

        // If NOT logged in → go to LoginForm.html
        if (session.getAttribute("activeuser") == null) {
            return "redirect:/login?needLogin";   // <-- this loads LoginForm.html
        }

        // If logged in → go to hackathon registration page 
        return "redirect:/hackathon/register-now";   // You can create this page later
    }
}
