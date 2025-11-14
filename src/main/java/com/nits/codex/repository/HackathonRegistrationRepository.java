package com.nits.codex.repository;

import com.nits.codex.model.HackathonRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HackathonRegistrationRepository 
        extends JpaRepository<HackathonRegistration, Long> {

    boolean existsByParticipantId(Long participantId);
}
