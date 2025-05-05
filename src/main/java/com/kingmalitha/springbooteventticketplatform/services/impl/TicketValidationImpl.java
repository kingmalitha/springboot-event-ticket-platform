package com.kingmalitha.springbooteventticketplatform.services.impl;

import com.kingmalitha.springbooteventticketplatform.domain.entities.*;
import com.kingmalitha.springbooteventticketplatform.exceptions.QrCodeNotFoundException;
import com.kingmalitha.springbooteventticketplatform.exceptions.TicketNotFoundException;
import com.kingmalitha.springbooteventticketplatform.repositories.QrCodeRepository;
import com.kingmalitha.springbooteventticketplatform.repositories.TicketRepository;
import com.kingmalitha.springbooteventticketplatform.repositories.TicketValidationRepository;
import com.kingmalitha.springbooteventticketplatform.services.TicketValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class TicketValidationImpl implements TicketValidationService {
    private final TicketValidationRepository ticketValidationRepository;
    private final QrCodeRepository qrCodeRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketValidation validateTicketByQrCode(UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository
                .findByIdAndStatus(qrCodeId, QrCodeStatusEnum.ACTIVE)
                .orElseThrow(
                () -> new QrCodeNotFoundException(
                        String.format("QR Code with id %s not found", qrCodeId.toString())
                ));

        Ticket ticket = qrCode.getTicket();
        return validateTicket(ticket);

    }

    private TicketValidation validateTicket(Ticket  ticket) {
        TicketValidation ticketValidation = new TicketValidation();
        ticketValidation.setTicket(ticket);
        ticketValidation.setValidationMethod(TicketValidationMethod.QR_SCAN);

        TicketValidationStatusEnum ticketValidationStatusEnum = ticket.getValidations().stream().filter(
                v -> TicketValidationStatusEnum.VALID.equals(v.getStatus())
        ).findFirst().map(
                v -> TicketValidationStatusEnum.INVALID
        ).orElse(TicketValidationStatusEnum.VALID);

        ticketValidation.setStatus(ticketValidationStatusEnum);

        return ticketValidationRepository.save(ticketValidation);
    }

    @Override
    public TicketValidation validateTicketManually(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(
                () -> new TicketNotFoundException(
                        String.format("Ticket with id %s not found", ticketId.toString())
                )
        );
        return validateTicket(ticket);
    }
}
