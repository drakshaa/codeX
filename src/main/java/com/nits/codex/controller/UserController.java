package com.nits.codex.controller;

import com.nits.codex.model.User;
import com.nits.codex.service.UserService;
import com.nits.codex.utils.VerifyRecaptcha;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@Slf4j
public class UserController {

    /** Set true in production to enforce reCAPTCHA verification */
    private static final boolean ENFORCE_CAPTCHA = false;

    @Autowired
    private UserService userService;

    /* ========================= LOGIN ========================= */

    @GetMapping({"/", "/login"})
    public String showLoginPage() {
        return "LoginForm";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam("identifier") String identifier,
                              @RequestParam("password") String password,
                              @RequestParam(value = "g-recaptcha-response", required = false) String gRCode,
                              HttpSession session,
                              RedirectAttributes ra) throws IOException {

        // reCAPTCHA check (optional in dev)
        if (!captchaOk(gRCode)) {
            log.warn("Login blocked: reCAPTCHA validation failed");
            ra.addFlashAttribute("message", "Please verify that you are not a robot.");
            ra.addFlashAttribute("messageType", "error");
            return "redirect:/login";
        }

        // Password hashing (MD5) – consider migrating to BCryptPasswordEncoder later
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = userService.userLogin(identifier, encryptedPassword);

        if (user != null) {
            log.info("Login success for {}", identifier);
            session.setAttribute("activeuser", user);
            session.setMaxInactiveInterval(600); // seconds
            return "redirect:/home";
        } else {
            log.info("Login failed for {}: invalid credentials", identifier);
            ra.addFlashAttribute("message", "User not found or incorrect password!");
            ra.addFlashAttribute("messageType", "error");
            return "redirect:/login";
        }
    }

    /* ========================= SIGNUP ========================= */

    @GetMapping("/signup")
    public String showSignupPage() {
        return "SignupForm";
    }

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute User u,
                               @RequestParam(value = "g-recaptcha-response", required = false) String gRCode,
                               RedirectAttributes ra) throws IOException {

        if (!captchaOk(gRCode)) {
            ra.addFlashAttribute("message", "Please verify that you are not a robot.");
            ra.addFlashAttribute("messageType", "error");
            return "redirect:/signup";
        }

        // Hash password (MD5) – consider BCrypt in production
        u.setPassword(DigestUtils.md5DigestAsHex(u.getPassword().getBytes()));

        boolean created = userService.userSignup(u);
        if (created) {
            log.info("Signup success for {}", u.getEmail());
            ra.addFlashAttribute("message", "Signup successful! Please log in.");
            ra.addFlashAttribute("messageType", "success");
            return "redirect:/login"; // PRG pattern
        } else {
            log.info("Signup failed for {}: duplicate username/email", u.getEmail());
            ra.addFlashAttribute("message", "Signup failed! Username or email may already exist.");
            ra.addFlashAttribute("messageType", "error");
            return "redirect:/signup";
        }
    }

    /* ========================= HOME & LOGOUT ========================= */

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("activeuser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("uname", user.getFname());
        return "Home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    /* ========================= HELPERS ========================= */

    private boolean captchaOk(String gRCode) throws IOException {
        if (!ENFORCE_CAPTCHA) return true; // allow during local/dev
        return gRCode != null && !gRCode.isBlank() && VerifyRecaptcha.verify(gRCode);
    }
}
