package com.nits.codex.serviceimpl;

import com.nits.codex.model.Submission;
import com.nits.codex.repository.SubmissionRepository;
import com.nits.codex.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {
    private final SubmissionRepository repo;

    @Override
    public Submission save(String roundId, Integer userId, Double score) {
        Submission s = new Submission();
        s.setRoundId(roundId);
        s.setUserId(userId);
        s.setScore(score);
        return repo.save(s);
    }

    @Override
    public List<Submission> top10() {
        return repo.findTop10ByOrderByScoreDescCreatedAtAsc();
    }

	@Override
	public void save(String roundId, int userId, double score) {
		// TODO Auto-generated method stub
		
	}
}
