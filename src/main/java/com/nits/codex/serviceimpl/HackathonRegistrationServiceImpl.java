package com.nits.codex.serviceimpl;

import com.nits.codex.model.HackathonRegistration;
import com.nits.codex.repository.HackathonRegistrationRepository;
import com.nits.codex.service.HackathonRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HackathonRegistrationServiceImpl implements HackathonRegistrationService {

    @Autowired
    private HackathonRegistrationRepository repo;

    @Override
    public HackathonRegistration register(HackathonRegistration reg) {
        return repo.save(reg);
    }

    @Override
    public boolean alreadyRegistered(Long participantId) {
        return repo.existsByParticipantId(participantId);
    }
}
