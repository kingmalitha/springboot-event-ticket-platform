package com.kingmalitha.springbooteventticketplatform.services;

import com.kingmalitha.springbooteventticketplatform.domain.entities.Ticket;
import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketType;
import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketValidation;

import java.util.UUID;

public interface TicketValidationService {
    TicketValidation validateTicketByQrCode(UUID qrCodeId);
    TicketValidation validateTicketManually(UUID ticketId);
}
