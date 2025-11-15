package com.nits.codex.serviceimpl;

import com.nits.codex.model.HackathonRegistration;
import com.nits.codex.repository.HackathonRegistrationRepository;
import com.nits.codex.service.HackathonRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

	@Override
	public boolean isTeamNameTaken(String teamName) {
		return repo.existsByTeamName(teamName);
	}

	@Override
	public boolean isTeamLeader(Long participantId) {
		return repo.existsByParticipantId(participantId);
	}

	@Override
	public boolean hasMcqCompleted(Long participantId) {
        Optional<HackathonRegistration> reg = repo.findByParticipantId(participantId);
        
		return reg.isPresent() && reg.get().isMcqCompleted();
	}

	@Override
	public void markMcqCompleted(Long participantId) {
        Optional<HackathonRegistration> regOptional = repo.findByParticipantId(participantId);
        
        if (regOptional.isPresent()) {
            HackathonRegistration reg = regOptional.get();
            reg.setMcqCompleted(true);
            repo.save(reg);
        }
	}

	@Override
	public String getSelectedProblemStatementId(Long participantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelectedProblemStatementId(Long participantId, String problemStatementId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasRound2Submitted(Long participantId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void markRound2Submitted(Long participantId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTeamNameByLeaderId(Long participantId) {
		Optional<HackathonRegistration> reg = repo.findByParticipantId(participantId);
        return reg.map(HackathonRegistration::getTeamName).orElse(null);
	}

	@Override
	public void setRound2Qualified(Long participantId, boolean status) {
		Optional<HackathonRegistration> regOptional = repo.findByParticipantId(participantId);
	    
	    if (regOptional.isPresent()) {
	        HackathonRegistration reg = regOptional.get();
	        reg.setRound2Qualified(status);
	        repo.save(reg);
	    }
		
	}

	@Override
	public boolean isRound2Qualified(Long participantId) {
		Optional<HackathonRegistration> reg = repo.findByParticipantId(participantId);
	    return reg.isPresent() && reg.get().isRound2Qualified();
	}
}