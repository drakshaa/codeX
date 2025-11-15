package com.nits.codex.controller;

import com.nits.codex.model.User;
import com.nits.codex.service.HackathonRegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private HackathonRegistrationService registrationService; 

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        
        User user = (User) session.getAttribute("activeuser");

        if (user != null) { 
            Long userId = (long) user.getId();
            
            boolean isRegistered = registrationService.alreadyRegistered(userId);
            boolean isTeamLeader = registrationService.isTeamLeader(userId);
            
            model.addAttribute("isRegistered", isRegistered);
            model.addAttribute("isTeamLeader", isTeamLeader);
            
            model.addAttribute("mcqCompleted", registrationService.hasMcqCompleted(userId));
            model.addAttribute("isRound2Qualified", registrationService.isRound2Qualified(userId));
            model.addAttribute("isAccommodationBooked", registrationService.isAccommodationBooked(userId));
        }

        return "Home"; 
    }

    @GetMapping("/join-us")
    public String joinUs(HttpSession session) {

        if (session.getAttribute("activeuser") == null) {
            return "redirect:/login?needLogin"; 
        }

        return "redirect:/hackathon/register-now";
    }
    
    @GetMapping("/about")
    public String aboutHackathon() {
        return "about-hackathon"; // This returns templates/about-hackathon.html
    }
}