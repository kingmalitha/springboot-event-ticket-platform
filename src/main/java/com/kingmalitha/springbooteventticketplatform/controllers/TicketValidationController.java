package com.kingmalitha.springbooteventticketplatform.controllers;

import com.kingmalitha.springbooteventticketplatform.domain.dtos.TicketValidationRequestDto;
import com.kingmalitha.springbooteventticketplatform.domain.dtos.TicketValidationResponseDto;
import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketValidation;
import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketValidationMethod;
import com.kingmalitha.springbooteventticketplatform.mappers.TicketValidationMapper;
import com.kingmalitha.springbooteventticketplatform.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validateTicket(
            @RequestBody TicketValidationRequestDto ticketValidationRequestDto
    ) {
        TicketValidationMethod method = ticketValidationRequestDto.getMethod();
        TicketValidation ticketValidation;

        if(TicketValidationMethod.QR_SCAN.equals(method)) {
           ticketValidation =  ticketValidationService.validateTicketByQrCode(
                    ticketValidationRequestDto.getId()
            );
        }else{
           ticketValidation = ticketValidationService.validateTicketManually(
                    ticketValidationRequestDto.getId());
        }
        return ResponseEntity.ok(
                ticketValidationMapper.toTicketValidationResponseDto(ticketValidation)
        );
    }
}
