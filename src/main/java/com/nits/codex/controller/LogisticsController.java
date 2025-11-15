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

@Controller
@RequestMapping("/hackathon") // Added RequestMapping to match frontend link
public class LogisticsController {
    
    @Autowired
    private HackathonRegistrationService registrationService; // Injected dependency

    @GetMapping("/accommodation")
    public String showAccommodationForm(HttpSession session, Model model) {
        
        User user = (User) session.getAttribute("activeuser");

        // SECURITY CHECK 1: User must be logged in
        if (user == null) {
            return "redirect:/login?needLogin";
        }
        
        Long userId = (long)user.getId();

        // SECURITY CHECK 2: User must be a qualified finalist
        if (!registrationService.isRound2Qualified(userId)) {
            return "redirect:/";
        }
        
        // Pass current booking status to the form
        model.addAttribute("isBooked", registrationService.isAccommodationBooked(userId));
        return "accommodation-form"; // Loads templates/accommodation-form.html 
    }

    @PostMapping("/accommodation")
    public String handleAccommodationBooking(HttpSession session, 
                                           @RequestParam("confirmation") String confirmation, 
                                           RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("activeuser");
        
        // Ensure user is logged in
        if (user == null) {
            return "redirect:/login";
        }

        Long userId = (long) user.getId();

        // Ensure user is qualified before updating database
        if (!registrationService.isRound2Qualified(userId)) {
            return "redirect:/";
        }
        
        if ("confirm_booking".equals(confirmation)) {
            // Mark as booked in the database
            registrationService.setAccommodationBooked(userId, true);
            redirectAttributes.addFlashAttribute("successMessage", "Accommodation confirmed for the onsite round!");
        }
        
        // Redirect back to the GET method to display the updated status
        return "redirect:/hackathon/accommodation";
    }
}