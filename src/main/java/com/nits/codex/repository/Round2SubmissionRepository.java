package com.nits.codex.repository;

import com.nits.codex.model.Round2Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Round2SubmissionRepository extends JpaRepository<Round2Submission, Long> {
    
    boolean existsByTeamLeaderId(Long teamLeaderId);
}