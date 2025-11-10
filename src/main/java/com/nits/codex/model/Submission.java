package com.nits.codex.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "submissions")
public class Submission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roundId;
    private Integer userId;              // or String username/email
    private Double score;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
