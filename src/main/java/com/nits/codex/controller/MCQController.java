package com.nits.codex.controller;

import com.nits.codex.model.Question;
import com.nits.codex.model.User;
import com.nits.codex.service.HackathonRegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class MCQController {

    @Autowired
    private HackathonRegistrationService registrationService;

    private List<Question> getMockQuestions() {
        return List.of(
            new Question(1L, "Which time complexity is better?", List.of("O(n^2)", "O(n log n)", "O(2^n)", "O(n!)"), "MCQ", "O(n log n)"),
            new Question(2L, "What is the time complexity of a hash map lookup?", List.of("O(1)", "O(n)", "O(log n)", "O(n^2)"), "MCQ", "O(1)"),
            new Question(3L, "What is the maximum number of nodes at level 'L' of a binary tree?", List.of("2^(L-1)", "2^L", "L^2", "L"), "MCQ", "2^(L-1)"),
            new Question(4L, "Describe the principle of Dynamic Programming.", List.of("Divide and Conquer", "Greedy Approach", "Optimal Substructure and Overlapping Subproblems", "Branch and Bound"), "MCQ", "Optimal Substructure and Overlapping Subproblems"),
            new Question(5L, "Write code to reverse a linked list.", null, "DSA", null) // End after Q5
        );
    }

    private static final long TOTAL_TIME_MINUTES = 60;

    @GetMapping("/mcq")
    public String startRound(HttpSession session, Model model) {
        List<Question> questions = getMockQuestions();
        
        if (session.getAttribute("startTime") == null) {
            session.setAttribute("startTime", System.currentTimeMillis());
            session.setAttribute("totalTimeMillis", TimeUnit.MINUTES.toMillis(TOTAL_TIME_MINUTES));
            session.setAttribute("questionList", questions);
            session.setAttribute("currentIndex", 0);
        }
        
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");
        
        if (currentIndex == null || currentIndex >= questions.size()) {
            return "redirect:/mcq/submit-round";
        }

        Question currentQuestion = questions.get(currentIndex);

        model.addAttribute("question", currentQuestion);
        model.addAttribute("totalQuestions", questions.size());
        model.addAttribute("currentIndex", currentIndex + 1);
        model.addAttribute("totalTime", TOTAL_TIME_MINUTES);

        return "mcq";
    }
    
    @PostMapping("/mcq/next")
    public String nextQuestion(@RequestParam(name = "answer", required = false) String answer, 
                               HttpSession session) {
        
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");
        List<Question> questions = (List<Question>) session.getAttribute("questionList");
        
        System.out.println("Saving answer for Q " + (currentIndex + 1) + ": " + answer); 

        session.setAttribute("currentIndex", currentIndex + 1);

        // Check for end of round (5 questions completed)
        if (currentIndex + 1 >= 5 || currentIndex + 1 >= questions.size()) {
            return "redirect:/mcq/submit-round";
        }

        return "redirect:/mcq";
    }
    
    @GetMapping("/mcq/submit-round")
    public String submitRound(HttpSession session, Model model) {
        
        User user = (User) session.getAttribute("activeuser");
        if (user != null) {
            registrationService.markMcqCompleted((long) user.getId()); 
        }

        session.removeAttribute("startTime");
        session.removeAttribute("questionList");
        session.removeAttribute("currentIndex");
        
        model.addAttribute("message", "Your answers have been submitted!");
        return "mcq-results";
    }
}