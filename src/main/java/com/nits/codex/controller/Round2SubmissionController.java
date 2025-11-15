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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hackathon")
public class Round2SubmissionController {

    @Autowired
    private HackathonRegistrationService registrationService;

    @GetMapping("/round2-submit")
    public String showRound2SubmissionForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("activeuser");

        if (user == null) {
            return "redirect:/login?needLogin";
        }

        Long userId = (long) user.getId();
        
        // Authorization Check: Must be the Team Leader and must have completed Round 1
        if (!registrationService.isTeamLeader(userId) || !registrationService.hasMcqCompleted(userId)) {
            return "redirect:/"; // Redirect to homepage if unauthorized/not qualified
        }
        
        // Check if already submitted
        if (registrationService.hasRound2Submitted(userId)) {
            return "redirect:/hackathon/round2-status";
        }

        return "round2-submit"; 
    }

    @PostMapping("/round2-submit")
    public String handleRound2Submission(@RequestParam("pptFile") MultipartFile pptFile, 
                                     @RequestParam("videoUrl") String videoUrl,
                                     @RequestParam("prototypeSummary") String prototypeSummary,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("activeuser");
        if (user == null) {
            return "redirect:/login";
        }
        Long userId = (long) user.getId();
        
        // File and URL validation check
        if (pptFile.isEmpty() || videoUrl.isBlank() || prototypeSummary.isBlank()) {
             redirectAttributes.addFlashAttribute("error", "All fields are required.");
             return "redirect:/hackathon/round2-submit";
        }
        
        // NOTE: Secure file storage logic (e.g., saving pptFile) goes here.

        // Mark submission complete in the database
        registrationService.markRound2Submitted(userId);
        
        // Redirect to status page
        return "redirect:/hackathon/round2-status"; 
    }
    
    @GetMapping("/round2-status")
    public String round2SubmissionStatus(Model model) {
        model.addAttribute("message", "Your prototype materials have been successfully submitted!");
        return "round2-status";
    }
}