package com.nits.codex.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class MCQSubmitRequest {
    @NotBlank
    private String roundId;

    private List<Integer> answers;
}
