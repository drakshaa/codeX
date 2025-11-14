package com.nits.codex.service;

import com.nits.codex.model.HackathonRegistration;

public interface HackathonRegistrationService {
    HackathonRegistration register(HackathonRegistration reg);

    boolean alreadyRegistered(Long participantId);
}
