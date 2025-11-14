package com.nits.codex.controller;

import com.nits.codex.model.HackathonRegistration;
import com.nits.codex.model.Participant;
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

    // show registration form
    @GetMapping("/register-now")
    public String showForm(HttpSession session, Model model) {

        Participant p = (Participant) session.getAttribute("activeParticipant");
        if (p == null) return "redirect:/login";

        if (service.alreadyRegistered(p.getId())) {
            model.addAttribute("success", "You have already registered!");
        }

        return "register-now";
    }

    // handle form submission
    @PostMapping("/register")
    public String submit(
            @ModelAttribute HackathonRegistration reg,
            HttpSession session,
            Model model) {

        Participant p = (Participant) session.getAttribute("activeParticipant");
        if (p == null) return "redirect:/login";

        if (service.alreadyRegistered(p.getId())) {
            model.addAttribute("success", "You have already registered!");
            return "register-now";
        }

        reg.setParticipantId(p.getId());
        service.register(reg);

        model.addAttribute("success", "Registration Successful!");
        return "register-now";
    }
}
