package com.nits.codex.controller;

import com.nits.codex.model.AccommodationBooking;
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
@RequestMapping("/hackathon") 
public class LogisticsController {
    
    @Autowired
    private HackathonRegistrationService registrationService; // Injected dependency

    @GetMapping("/accommodation")
    public String showAccommodationForm(HttpSession session, Model model) {
        
        User user = (User) session.getAttribute("activeuser");

        if (user == null) {
            return "redirect:/login?needLogin";
        }
        
        Long userId = (long)user.getId();

        if (!registrationService.isRound2Qualified(userId)) {
            return "redirect:/";
        }
        
        model.addAttribute("isBooked", registrationService.isAccommodationBooked(userId));
        return "accommodation-form"; // Loads templates/accommodation-form.html 
    }

    
    @PostMapping("/accommodation")
    public String handleAccommodationBooking(HttpSession session, 
                                           @RequestParam("confirmation") String confirmation, 
                                           @RequestParam(name = "numMembers", required = false) Integer numMembers, // Accept new field
                                           RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("activeuser");
        Long userId = (long) user.getId();

        if (user == null || !registrationService.isRound2Qualified(userId)) {
            return "redirect:/";
        }
        
        if ("confirm_booking".equals(confirmation)) {
            if (numMembers == null || numMembers <= 0 || numMembers > 3) {
                 redirectAttributes.addFlashAttribute("errorMessage", "Please enter a valid number of team members (1-3).");
                 return "redirect:/hackathon/accommodation";
            }

            String teamName = registrationService.getTeamNameByLeaderId(userId); 
            
            AccommodationBooking booking = new AccommodationBooking();
            booking.setTeamLeaderId(userId);
            booking.setTeamName(teamName);
            booking.setNumberOfMembers(numMembers);
            booking.setRequiresBooking(true);
            
            registrationService.saveAccommodationDetails(booking);
            
            registrationService.setAccommodationBooked(userId, true);
            
            redirectAttributes.addFlashAttribute("successMessage", "Accommodation confirmed for " + numMembers + " members.");
        } else {
             registrationService.setAccommodationBooked(userId, false); 
             redirectAttributes.addFlashAttribute("successMessage", "Accommodation confirmed as NOT required.");
        }
        
        return "redirect:/hackathon/accommodation";
    }
    
    @PostMapping("/accommodation/cancel")
    public String handleAccommodationCancellation(HttpSession session, RedirectAttributes redirectAttributes) {
        
        User user = (User) session.getAttribute("activeuser");
        if (user == null || !registrationService.isRound2Qualified((long)user.getId())) {
            return "redirect:/";
        }
        
        Long userId = (long) user.getId();
        
        registrationService.cancelAccommodationBooking(userId);
        
        redirectAttributes.addFlashAttribute("successMessage", "Accommodation booking successfully canceled.");
        
       
        return "redirect:/hackathon/accommodation";
    }
}