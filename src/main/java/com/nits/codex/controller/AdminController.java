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

    
    @Autowired
    private HackathonRegistrationService registrationService;
    
    
    @Autowired
    private Round2SubmissionRepository submissionRepository; 

    

    @GetMapping("/login")
    public String showAdminLogin() {
        return "admin-login"; 
    }

    @PostMapping("/login")
    public String processAdminLogin(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        
        
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

   

    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        
        
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/login";
        }
        
      
        model.addAttribute("submissions", submissionRepository.findAll()); 
        
      
        
        return "admin-dashboard";
    }

    @PostMapping("/qualify-team")
    public String qualifyTeam(@RequestParam("leaderId") Long leaderId, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        
       
        if (session.getAttribute("isAdmin") == null) {
            return "redirect:/admin/login";
        }
        
        if (registrationService.alreadyRegistered(leaderId)) {
           
            registrationService.setRound2Qualified(leaderId, true);
            redirectAttributes.addFlashAttribute("adminSuccess", 
                                                 "Team promoted: ID " + leaderId + " is now a Finalist.");
        } else {
            redirectAttributes.addFlashAttribute("adminError", 
                                                "Error: No registration found for ID " + leaderId + ".");
        }
        
        return "redirect:/admin/dashboard"; 
    }
    
    @PostMapping("/verify-payment")
    public String verifyPayment(@RequestParam("leaderId") Long leaderId, 
                                RedirectAttributes redirectAttributes) {
        
       
        
        if (registrationService.alreadyRegistered(leaderId)) {
            registrationService.setVerifiedStatus(leaderId, true);
            redirectAttributes.addFlashAttribute("adminSuccess", 
                                                 "Payment verified and registration activated for ID " + leaderId + ".");
        } else {
            redirectAttributes.addFlashAttribute("adminError", "Error: Registration not found.");
        }
        
        return "redirect:/admin/dashboard";
    }
}