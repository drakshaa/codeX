package com.nits.codex.service;

import java.util.List;

import com.nits.codex.api.dto.MCQRound;

public interface MCQService {
    MCQRound generateRound(String roundType);
    double evaluate(String roundId, List<Integer> answers);
}
