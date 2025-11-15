package com.nits.codex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AccommodationBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long teamLeaderId;
    private String teamName;
    private int numberOfMembers; 
    private String contactPhone;
    private boolean requiresBooking = false; 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTeamLeaderId() {
		return teamLeaderId;
	}
	public void setTeamLeaderId(Long teamLeaderId) {
		this.teamLeaderId = teamLeaderId;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getNumberOfMembers() {
		return numberOfMembers;
	}
	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public boolean isRequiresBooking() {
		return requiresBooking;
	}
	public void setRequiresBooking(boolean requiresBooking) {
		this.requiresBooking = requiresBooking;
	}

   
}