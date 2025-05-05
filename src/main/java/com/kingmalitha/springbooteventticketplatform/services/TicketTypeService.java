package com.kingmalitha.springbooteventticketplatform.services;

import com.kingmalitha.springbooteventticketplatform.domain.entities.Ticket;

import java.util.UUID;

public interface TicketTypeService {

    Ticket purchaseTicket(UUID userId, UUID ticketTypeId);
}
