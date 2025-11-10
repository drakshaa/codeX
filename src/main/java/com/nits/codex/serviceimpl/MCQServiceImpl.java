package com.nits.codex.serviceimpl;

import com.nits.codex.api.dto.MCQQuestion;
import com.nits.codex.api.dto.MCQRound;
import com.nits.codex.service.MCQService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MCQServiceImpl implements MCQService {

    @Override
    public MCQRound generateRound(String roundType) {

        MCQRound round = new MCQRound();
        round.setRoundId(UUID.randomUUID().toString());

        // --- Sample Questions (replace with dynamic ones later) ---
        MCQQuestion q1 = new MCQQuestion();
        q1.setId("Q1");
        q1.setQuestion("Which time complexity is better?");
        q1.setOptions(List.of("O(n^2)", "O(n log n)", "O(2^n)", "O(n!)"));
        q1.setCorrectIndex(1);

        MCQQuestion q2 = new MCQQuestion();
        q2.setId("Q2");
        q2.setQuestion("Which HTTP method is idempotent?");
        q2.setOptions(List.of("POST", "PUT", "PATCH", "CONNECT"));
        q2.setCorrectIndex(1);

        round.setQuestions(List.of(q1, q2));

        return round;
    }

    @Override
    public double evaluate(String roundId, List<Integer> answers) {
        int[] correct = {1, 1};
        int score = 0;

        for (int i = 0; i < correct.length && i < answers.size(); i++) {
            if (answers.get(i) != null && answers.get(i) == correct[i]) {
                score++;
            }
        }

        return (score * 100.0) / correct.length;
    }
}
