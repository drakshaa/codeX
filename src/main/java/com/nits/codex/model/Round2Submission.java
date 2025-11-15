package com.nits.codex.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Round2Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

 
    private Long teamLeaderId;
    private String teamName;
    private String selectedProblemStatementId;

    
    private String pptFilePath; 
    private String videoUrl;
    private String prototypeSummary;
    private LocalDateTime submissionDate;

    public Round2Submission() {
        this.submissionDate = LocalDateTime.now();
    }

    
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getTeamLeaderId() { return teamLeaderId; }
    public void setTeamLeaderId(Long teamLeaderId) { this.teamLeaderId = teamLeaderId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getSelectedProblemStatementId() { return selectedProblemStatementId; }
    public void setSelectedProblemStatementId(String selectedProblemStatementId) { this.selectedProblemStatementId = selectedProblemStatementId; }

    public String getPptFilePath() { return pptFilePath; }
    public void setPptFilePath(String pptFilePath) { this.pptFilePath = pptFilePath; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getPrototypeSummary() { return prototypeSummary; }
    public void setPrototypeSummary(String prototypeSummary) { this.prototypeSummary = prototypeSummary; }

    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }
}