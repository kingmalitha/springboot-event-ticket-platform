package com.kingmalitha.springbooteventticketplatform.repositories;

import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketValidation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketValidationRepository extends JpaRepository<TicketValidation, UUID> {
}
