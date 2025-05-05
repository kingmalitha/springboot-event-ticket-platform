package com.kingmalitha.springbooteventticketplatform.mappers;

import com.kingmalitha.springbooteventticketplatform.domain.CreateEventRequest;
import com.kingmalitha.springbooteventticketplatform.domain.CreateTicketTypeRequest;
import com.kingmalitha.springbooteventticketplatform.domain.UpdateEventRequest;
import com.kingmalitha.springbooteventticketplatform.domain.dtos.*;
import com.kingmalitha.springbooteventticketplatform.domain.entities.Event;
import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

    ListEventTicketTypeResponseDto toDto(TicketType ticketType);

    ListEventResponseDto toListEventResponseDto(Event event);

    GetEventDetailsTicketTypesResponseDto toGetEventDetailsTicketTypesResponseDto(TicketType ticketType);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);

    UpdateTicketTypeRequestDto fromDto(UpdateTicketTypeRequestDto dto);

    UpdateEventRequest fromDto(UpdateEventRequestDto dto);

    UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);

    UpdateEventResponseDto toUpdateEventResponseDto(Event event);

    ListPublishedEventResponseDto toListPublishedEventResponseDto(Event event);

    GetPublishedEventDetailsTicketTypesResponseDto toGetPublishedEventDetailsTicketTypesResponseDto(TicketType ticketType);

    GetPublishedEventDetailsResponseDto toGetPublishedEventDetailsResponseDto(Event event);

}
