package com.kingmalitha.springbooteventticketplatform.services;

import com.kingmalitha.springbooteventticketplatform.domain.entities.QrCode;
import com.kingmalitha.springbooteventticketplatform.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
