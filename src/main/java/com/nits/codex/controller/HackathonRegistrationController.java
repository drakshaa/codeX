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

        // ⭐ FIX 1: Always add the empty model object for Thymeleaf's th:object
        if (!model.containsAttribute("hackathonRegistration")) {
            model.addAttribute("hackathonRegistration", new HackathonRegistration());
        }

        // Check if the user is already registered before showing the form
        if (service.alreadyRegistered((long) user.getId())) {
            model.addAttribute("error", "You have already registered for the hackathon.");
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

        // 1. Check if user is already registered
        if (service.alreadyRegistered((long) user.getId())) {
            model.addAttribute("error", "You have already registered for the hackathon.");
            return "register-now";
        }
        
        // 2. Check if Team Name is already taken
        if (service.isTeamNameTaken(reg.getTeamName())) {
            model.addAttribute("error", "Error: The team name '" + reg.getTeamName() + "' is already taken. Please choose a different name.");
            return "register-now";
        }

        // --- If all checks pass, proceed with registration ---
        reg.setParticipantId((long) user.getId());
        
        // This line stores the data in the database
        service.register(reg);

        // ✅ FIX APPLIED: Use Post/Redirect/Get (PRG) pattern. 
        // Redirects to a clean GET endpoint after successful POST.
        return "redirect:/hackathon/register-success";
    }
    @GetMapping("/register-success")
    public String registrationSuccess(Model model) {
        // You can use the flash attributes here if needed, but for a simple 
        // static message, we'll just return a success view.
        
        // This ensures the view logic is separate from the form logic.
        return "registration-success"; 
    }
}