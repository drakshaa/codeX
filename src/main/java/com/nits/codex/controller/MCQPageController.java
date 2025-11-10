package com.nits.codex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MCQPageController {

    @GetMapping("/mcq")
    public String mcqPage() {
        return "mcq"; // Looks for templates/mcq.html
    }
}
