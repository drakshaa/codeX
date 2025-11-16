package com.nits.codex.serviceimpl;

import com.nits.codex.model.HackathonRegistration;
import com.nits.codex.model.AccommodationBooking;
import com.nits.codex.repository.HackathonRegistrationRepository;
import com.nits.codex.repository.AccommodationBookingRepository;
import com.nits.codex.service.HackathonRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for delete operation

import java.util.Optional;

@Service
public class HackathonRegistrationServiceImpl implements HackathonRegistrationService {

    @Autowired
    private HackathonRegistrationRepository repo; 
    
    @Autowired
    private AccommodationBookingRepository bookingRepository; // Properly injected

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

    // --- Round 2 Submission Methods ---

	@Override
	public String getSelectedProblemStatementId(Long participantId) {
        Optional<HackathonRegistration> reg = repo.findByParticipantId(participantId);
        return reg.map(HackathonRegistration::getSelectedProblemStatementId).orElse(null);
	}

	@Override
	public void setSelectedProblemStatementId(Long participantId, String problemStatementId) {
        Optional<HackathonRegistration> regOptional = repo.findByParticipantId(participantId);
        
        if (regOptional.isPresent()) {
            HackathonRegistration reg = regOptional.get();
            reg.setSelectedProblemStatementId(problemStatementId);
            repo.save(reg);
        }
	}

	@Override
	public boolean hasRound2Submitted(Long participantId) {
        Optional<HackathonRegistration> reg = repo.findByParticipantId(participantId);
		return reg.isPresent() && reg.get().isRound2Submitted();
	}

	@Override
	public void markRound2Submitted(Long participantId) {
        Optional<HackathonRegistration> regOptional = repo.findByParticipantId(participantId);
        
        if (regOptional.isPresent()) {
            HackathonRegistration reg = regOptional.get();
            reg.setRound2Submitted(true);
            repo.save(reg);
        }
	}

    // --- Finalist & Qualification Methods ---

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
    
    // --- Accommodation Booking Methods ---

	@Override
	public boolean isAccommodationBooked(Long participantId) {
        Optional<HackathonRegistration> reg = repo.findByParticipantId(participantId);
		return reg.isPresent() && reg.get().isAccommodationBooked();
	}

	@Override
	public void setAccommodationBooked(Long participantId, boolean booked) {
        Optional<HackathonRegistration> regOptional = repo.findByParticipantId(participantId);
        
        if (regOptional.isPresent()) {
            HackathonRegistration reg = regOptional.get();
            reg.setAccommodationBooked(booked);
            repo.save(reg);
        }
	}

	@Override
	public void saveAccommodationDetails(AccommodationBooking booking) {
		bookingRepository.save(booking);
	}

	@Transactional 
	@Override
	public void cancelAccommodationBooking(Long teamLeaderId) {
	    
	    bookingRepository.deleteByTeamLeaderId(teamLeaderId); 

	    setAccommodationBooked(teamLeaderId, false);
	}

	@Override
	public boolean isVerified(Long participantId) {
		Optional<HackathonRegistration> reg = repo.findByParticipantId(participantId);
	    return reg.isPresent() && reg.get().isVerified();
	}

	@Override
	public void setVerifiedStatus(Long participantId, boolean status) {
		Optional<HackathonRegistration> regOptional = repo.findByParticipantId(participantId);
	    
	    if (regOptional.isPresent()) {
	        HackathonRegistration reg = regOptional.get();
	        reg.setVerified(status);
	        repo.save(reg);
	    }
	}
}