package com.nits.codex.service;

import com.nits.codex.model.HackathonRegistration;

public interface HackathonRegistrationService {
	HackathonRegistration register(HackathonRegistration reg);

	boolean alreadyRegistered(Long Participantid);

	boolean isTeamNameTaken(String teamName);

	boolean isTeamLeader(Long participantId);

	boolean hasMcqCompleted(Long participantId);

	void markMcqCompleted(Long participantId);

	String getSelectedProblemStatementId(Long participantId);

	void setSelectedProblemStatementId(Long participantId, String problemStatementId);
	
	boolean hasRound2Submitted(Long participantId); 
    
    void markRound2Submitted(Long participantId); 
    
    String getTeamNameByLeaderId(Long participantId);
    boolean isRound2Qualified(Long participantId);
    void setRound2Qualified(Long participantId, boolean status);
    boolean isAccommodationBooked(Long participantId);
    void setAccommodationBooked(Long participantId, boolean booked);
}

