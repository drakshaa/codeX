package com.nits.codex.controller;

import com.nits.codex.model.Round2Submission;
import com.nits.codex.model.User;
import com.nits.codex.repository.Round2SubmissionRepository; // ⭐ New Repository
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
    
    @Autowired
    private Round2SubmissionRepository submissionRepository; // ⭐ New Submission Repository

    // ... (showRound2SubmissionForm and round2SubmissionStatus remain unchanged) ...

    @GetMapping("/round2-submit")
    public String showRound2SubmissionForm(HttpSession session, Model model) {
        // ... (existing authorization logic) ...
        User user = (User) session.getAttribute("activeuser");
        if (user == null) { return "redirect:/login?needLogin"; }
        Long userId = (long) user.getId();
        if (!registrationService.isTeamLeader(userId) || !registrationService.hasMcqCompleted(userId)) { return "redirect:/"; }
        if (registrationService.hasRound2Submitted(userId)) { return "redirect:/hackathon/round2-status"; }
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
        
        // 1. Validation (ensure required fields are not empty)
        if (pptFile.isEmpty() || videoUrl.isBlank() || prototypeSummary.isBlank()) {
             redirectAttributes.addFlashAttribute("error", "All fields are required.");
             return "redirect:/hackathon/round2-submit";
        }
        
        // --- 2. Data Retrieval and Storage ---
        
        // Mock file path creation (In production, use secure file system/S3)
        String pptFilename = "submission_" + userId + "_" + System.currentTimeMillis() + "_" + pptFile.getOriginalFilename();
        
        String teamName = registrationService.getTeamNameByLeaderId(userId);
        String psId = registrationService.getSelectedProblemStatementId(userId);

        // Create and populate the submission model
        Round2Submission submission = new Round2Submission();
        submission.setTeamLeaderId(userId);
        submission.setTeamName(teamName != null ? teamName : "N/A");
        submission.setSelectedProblemStatementId(psId);
        submission.setPptFilePath(pptFilename);
        submission.setVideoUrl(videoUrl);
        submission.setPrototypeSummary(prototypeSummary);
        
        // Save to the database
        submissionRepository.save(submission);

        // 3. Mark submission complete in the HackathonRegistration table
        registrationService.markRound2Submitted(userId);
        
        return "redirect:/hackathon/round2-status"; 
    }
    
    @GetMapping("/round2-status")
    public String round2SubmissionStatus(Model model) {
        model.addAttribute("message", "Your prototype materials have been successfully submitted!");
        return "round2-status";
    }
}