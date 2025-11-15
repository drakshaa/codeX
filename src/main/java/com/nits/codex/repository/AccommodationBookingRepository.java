package com.nits.codex.repository;

import com.nits.codex.model.AccommodationBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationBookingRepository extends JpaRepository<AccommodationBooking, Long> {
    boolean existsByTeamLeaderId(Long teamLeaderId);
    void deleteByTeamLeaderId(Long teamLeaderId);
}