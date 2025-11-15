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

    /**
     * Handles the GET request to display the initial registration form.
     */
    @GetMapping("/register-now")
    public String showForm(HttpSession session, Model model) {

        User user = (User) session.getAttribute("activeuser");

        if (user == null)
            return "redirect:/login?needLogin";

        // ⭐ CRITICAL FIX: Ensure the hackathonRegistration object is always present 
        // to prevent Thymeleaf parsing errors on th:object and th:field.
        if (!model.containsAttribute("hackathonRegistration")) {
            model.addAttribute("hackathonRegistration", new HackathonRegistration());
        }

        // Check if already registered before showing the form
        if (service.alreadyRegistered((long) user.getId())) {
            model.addAttribute("success", "You have already registered!");
        }
        
        return "register-now";   
    }


    /**
     * Handles the POST request for form submission and validation.
     */
    @PostMapping("/register")
    public String submit(@ModelAttribute HackathonRegistration reg,
                         HttpSession session,
                         Model model) {

        User user = (User) session.getAttribute("activeuser");

        if (user == null)
            return "redirect:/login?needLogin";

        // Check 1: Anti-Abuse (Already Registered)
        if (service.alreadyRegistered((long) user.getId())) {
            model.addAttribute("success", "You have already registered!");
            return "register-now";
        }
        
        // Check 2: Payment Confirmation (Transaction ID provided)
        if (reg.getTransactionId() == null || reg.getTransactionId().trim().isEmpty()) {
            model.addAttribute("error", "Payment Transaction ID is required to complete registration.");
            // Re-add the submitted object so user inputs aren't lost on error
            model.addAttribute("hackathonRegistration", reg);
            return "register-now";
        }

        // Simulate payment success and set flags
        reg.setPaid(true);
        reg.setParticipantId((long) user.getId());
        
        // Check 3: Team Name Uniqueness
        if (service.isTeamNameTaken(reg.getTeamName())) {
            model.addAttribute("error", "Error: The team name '" + reg.getTeamName() + "' is already taken. Please choose a different name.");
            // Re-add the submitted object so user inputs aren't lost on error
            model.addAttribute("hackathonRegistration", reg);
            return "register-now";
        }
        
        // Final Registration Save
        service.register(reg);

        model.addAttribute("success", "Registration Successful! Payment confirmed.");
        return "register-now";
    }
}