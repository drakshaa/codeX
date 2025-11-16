package com.nits.codex.controller;

import com.nits.codex.model.HackathonRegistration;
import com.nits.codex.model.User;
import com.nits.codex.service.HackathonRegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hackathon")
public class HackathonRegistrationController {

    @Autowired
    private HackathonRegistrationService service;

   
    @GetMapping("/register-now")
    public String showForm(HttpSession session, Model model) {

        User user = (User) session.getAttribute("activeuser");

        if (user == null)
            return "redirect:/login?needLogin";

        
        if (!model.containsAttribute("hackathonRegistration")) {
            model.addAttribute("hackathonRegistration", new HackathonRegistration());
        }

        if (service.alreadyRegistered((long) user.getId())) {
            model.addAttribute("success", "You have already registered!");
        }
        
        return "register-now";   
    }


   
    @PostMapping("/register")
    public String submit(@ModelAttribute HackathonRegistration reg,
                         HttpSession session,
                         Model model) {

        User user = (User) session.getAttribute("activeuser");

        if (user == null)
            return "redirect:/login?needLogin";

        if (service.alreadyRegistered((long) user.getId())) {
            model.addAttribute("success", "You have already registered!");
            return "register-now";
        }
        
        if (reg.getTransactionId() == null || reg.getTransactionId().trim().isEmpty()) {
            model.addAttribute("error", "Payment Transaction ID is required to complete registration.");
            model.addAttribute("hackathonRegistration", reg);
            return "register-now";
        }

        
        reg.setPaid(true);
        reg.setParticipantId((long) user.getId());
        
        
        if (service.isTeamNameTaken(reg.getTeamName())) {
            model.addAttribute("error", "Error: The team name '" + reg.getTeamName() + "' is already taken. Please choose a different name.");
            model.addAttribute("hackathonRegistration", reg);
            return "register-now";
        }
        
       
        service.register(reg);

        model.addAttribute("success", "Registration Successful! Payment confirmed.");
        return "register-now";
    }
}