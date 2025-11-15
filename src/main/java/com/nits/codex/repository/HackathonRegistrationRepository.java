package com.nits.codex.repository;

import com.nits.codex.model.HackathonRegistration;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HackathonRegistrationRepository 
        extends JpaRepository<HackathonRegistration, Long> {

    boolean existsByParticipantId(Long participantId);
    
    Optional<HackathonRegistration> findByParticipantId(Long participantId);
    boolean existsByTeamName(String teamName);
}
