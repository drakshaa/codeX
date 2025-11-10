package com.nits.codex.api;

import com.nits.codex.api.dto.ApiResponse;
import com.nits.codex.api.dto.MCQRound;
import com.nits.codex.api.dto.MCQSubmitRequest;
import com.nits.codex.service.MCQService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mcq")
@RequiredArgsConstructor
public class MCQRestController {

    private final MCQService mcqService;

    @PostMapping("/round")
    public ApiResponse<MCQRound> createRound() {
        return ApiResponse.ok(mcqService.generateRound("R1"));
    }

    @PostMapping("/submit")
    public ApiResponse<Double> submit(@RequestBody MCQSubmitRequest req) {
        double score = mcqService.evaluate(req.getRoundId(), req.getAnswers());
        return ApiResponse.ok(score);
    }
}
