package com.nits.codex.model;

import java.util.List;

public class Question {
    private Long id;
    private String text;
    private List<String> options; // Null for DSA questions
    private String type;         // "MCQ" or "DSA"
    private String answer;       // Correct answer (for internal scoring)

    // Constructor used for the mock data in the Controller
    public Question(Long id, String text, List<String> options, String type, String answer) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.type = type;
        this.answer = answer;
    }

    // Default Constructor
    public Question() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}