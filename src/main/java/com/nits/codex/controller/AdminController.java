package com.nits.codex.controller;

import com.nits.codex.repository.Round2SubmissionRepository;
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

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Inject necessary services and repositories
    @Autowired
    private HackathonRegistrationService registrationService;
    
    // Required to fetch Round 2 submissions for review
    @Autowired
    private Round2SubmissionRepository submissionRepository; 

    // --- Admin Login Handlers ---

    @GetMapping("/login")
    public String showAdminLogin() {
        return "admin-login"; // Loads templates/admin-login.html
    }

    @PostMapping("/login")
    public String processAdminLogin(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        
        // ⭐ SIMPLIFIED SECURITY CHECK (Hardcoded credentials)
        if ("admin_nits".equals(username) && "nits@2026".equals(password)) {
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/dashboard";
        }
        
        redirectAttributes.addFlashAttribute("error", "Invalid Admin Credentials.");
        return "redirect:/admin/login";
    }
    
    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.removeAttribute("isAdmin");
        return "redirect:/admin/login";
    }

    // --- Admin Dashboard & Core Logic ---

    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        
        // ⭐ SECURITY GATE
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/login";
        }
        
        // Fetch all submissions for review
        model.addAttribute("submissions", submissionRepository.findAll()); 
        
        // We will need to fetch the associated registration status later if needed, 
        // but for now, we just list the submissions.
        
        return "admin-dashboard";
    }

    @PostMapping("/qualify-team")
    public String qualifyTeam(@RequestParam("leaderId") Long leaderId, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        
        // ⭐ SECURITY GATE
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/login";
        }
        
        if (registrationService.alreadyRegistered(leaderId)) {
            // Set the qualification status to true
            registrationService.setRound2Qualified(leaderId, true);
            redirectAttributes.addFlashAttribute("adminSuccess", 
                                                 "Team promoted: ID " + leaderId + " is now a Finalist.");
        } else {
            redirectAttributes.addFlashAttribute("adminError", 
                                                "Error: No registration found for ID " + leaderId + ".");
        }
        
        return "redirect:/admin/dashboard"; 
    }
}