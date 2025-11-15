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

            // 1. Get Team Details (Assuming you have a method to fetch contact/team name)
            String teamName = registrationService.getTeamNameByLeaderId(userId); 
            
            // 2. Create and populate the booking record
            AccommodationBooking booking = new AccommodationBooking();
            booking.setTeamLeaderId(userId);
            booking.setTeamName(teamName);
            booking.setNumberOfMembers(numMembers);
            booking.setRequiresBooking(true);
            // Set contact phone/email from the User/HackathonRegistration objects if needed
            
            // 3. Save details to the new table
            registrationService.saveAccommodationDetails(booking);
            
            // 4. Mark the primary status as booked (if not already done in service)
            registrationService.setAccommodationBooked(userId, true);
            
            redirectAttributes.addFlashAttribute("successMessage", "Accommodation confirmed for " + numMembers + " members.");
        } else {
             // If "No, We Will Arrange Our Own" is clicked, mark as NOT required
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
        
        // Call the service to cancel the booking
        registrationService.cancelAccommodationBooking(userId);
        
        redirectAttributes.addFlashAttribute("successMessage", "Accommodation booking successfully canceled.");
        
        // Redirect back to the form to show the cancellation status
        return "redirect:/hackathon/accommodation";
    }
}