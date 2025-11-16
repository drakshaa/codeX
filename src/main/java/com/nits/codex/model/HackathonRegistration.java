package com.nits.codex.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hackathon_registrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HackathonRegistration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String teamName;

	private String leaderName;

	private String college;

	private String leaderEmail;

	private String leaderPhone;

	private int leaderAge;

	@Column(length = 1000)
	private String teamMembers; // comma separated list

	private Long participantId;

	private boolean mcqCompleted = false;

	private boolean round2Qualified = false;
	private boolean round2Submitted = false; 

	private String selectedProblemStatementId;
	
	private boolean isPaid = false; 
    private String transactionId;
    private boolean accommodationBooked = false;
    private boolean isVerified = false;
}
