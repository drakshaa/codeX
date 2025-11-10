package com.nits.codex.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class MCQQuestion {
    private String id;
    private String question;
    private List<String> options;
    private int correctIndex;
}
