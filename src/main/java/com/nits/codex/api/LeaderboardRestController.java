package com.nits.codex.api;

import com.nits.codex.api.dto.ApiResponse;
import com.nits.codex.model.Submission;
import com.nits.codex.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardRestController {
    private final LeaderboardService leaderboard;

    @GetMapping("/top")
    public ApiResponse<List<Submission>> top() {
        return ApiResponse.ok(leaderboard.top10());
    }
}
