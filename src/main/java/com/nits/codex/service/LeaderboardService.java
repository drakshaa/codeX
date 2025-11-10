package com.nits.codex.service;

import com.nits.codex.model.Submission;
import java.util.List;

public interface LeaderboardService {
    Submission save(String roundId, Integer userId, Double score);
    List<Submission> top10();
	void save(String roundId, int userId, double score);
}
