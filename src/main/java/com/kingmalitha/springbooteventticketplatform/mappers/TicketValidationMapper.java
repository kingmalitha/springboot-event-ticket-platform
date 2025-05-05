package com.kingmalitha.springbooteventticketplatform.mappers;

import com.kingmalitha.springbooteventticketplatform.domain.dtos.TicketValidationResponseDto;
import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper {

    @Mapping(target = "ticketId", source = "ticket.id")
    TicketValidationResponseDto toTicketValidationResponseDto(TicketValidation ticketValidation);

}
