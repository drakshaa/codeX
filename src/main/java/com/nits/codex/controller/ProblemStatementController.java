package com.nits.codex.controller;

import com.nits.codex.model.User;
import com.nits.codex.service.HackathonRegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/hackathon")
public class ProblemStatementController {

    @Autowired
    private HackathonRegistrationService registrationService;

    private List<ProblemStatement> getMockProblemStatements() {
        return Arrays.asList(
            new ProblemStatement("PS001", "AI-driven Waste Management", "Develop an AI solution to optimize waste collection routes or enhance sorting efficiency."),
            new ProblemStatement("PS002", "Smart Traffic Control System", "Design a machine learning model to predict traffic congestion and adjust signals in real-time."),
            new ProblemStatement("PS003", "Personalized Learning Platform", "Create an adaptive learning system that tailors content based on student performance using ML."),
            new ProblemStatement("PS004", "Disaster Prediction & Alert System", "Build a system using sensor data/satellite imagery and AI to predict natural disasters and issue early warnings.")
        );
    }

    @GetMapping("/select-problem-statement")
    public String showProblemStatements(HttpSession session, Model model) {
        User user = (User) session.getAttribute("activeuser");

        if (user == null) {
            return "redirect:/login?needLogin";
        }

        Long userId = (long) user.getId();
        
        if (!registrationService.isTeamLeader(userId) || !registrationService.hasMcqCompleted(userId)) {
            return "redirect:/"; 
        }

        if (registrationService.getSelectedProblemStatementId(userId) != null) {
            return "redirect:/hackathon/round2-submit";
        }

        model.addAttribute("problemStatements", getMockProblemStatements());
        return "select-problem-statement";
    }
    
    @PostMapping("/select-problem-statement")
    public String selectProblemStatement(@RequestParam("problemStatementId") String problemStatementId,
                                         HttpSession session,
                                         RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("activeuser");

        if (user == null) {
            return "redirect:/login";
        }
        
        Long userId = (long) user.getId();

        registrationService.setSelectedProblemStatementId(userId, problemStatementId);
        
        redirectAttributes.addFlashAttribute("successFlash", "Problem statement '" + problemStatementId + "' selected successfully! You can now proceed to prototype submission.");
        return "redirect:/hackathon/round2-submit";
    }

    private static class ProblemStatement {
        private String id;
        private String title;
        private String description;

        public ProblemStatement(String id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
    }
}