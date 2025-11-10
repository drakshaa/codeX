package com.nits.codex.api.dto;

import lombok.Data;
import java.util.List;

@Data
public class MCQRound {
    private String roundId;
    private List<MCQQuestion> questions;
}
