package com.kingmalitha.springbooteventticketplatform.services.impl;

import com.kingmalitha.springbooteventticketplatform.domain.CreateEventRequest;
import com.kingmalitha.springbooteventticketplatform.domain.UpdateEventRequest;
import com.kingmalitha.springbooteventticketplatform.domain.UpdateTicketTypeRequest;
import com.kingmalitha.springbooteventticketplatform.domain.entities.Event;
import com.kingmalitha.springbooteventticketplatform.domain.entities.EventStatusEnum;
import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketType;
import com.kingmalitha.springbooteventticketplatform.domain.entities.User;
import com.kingmalitha.springbooteventticketplatform.exceptions.EventNotFoundException;
import com.kingmalitha.springbooteventticketplatform.exceptions.EventUpdateException;
import com.kingmalitha.springbooteventticketplatform.exceptions.TicketTypeNotFoundException;
import com.kingmalitha.springbooteventticketplatform.exceptions.UserNotFoundException;
import com.kingmalitha.springbooteventticketplatform.repositories.EventRepository;
import com.kingmalitha.springbooteventticketplatform.repositories.UserRepository;
import com.kingmalitha.springbooteventticketplatform.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl  implements EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User user = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %s not found", organizerId)));

        Event eventToCreate  = new Event();
        List<TicketType> ticketTypesToCreate =
                event.getTicketTypes().stream().map(
                ticketType -> {
                    TicketType ticketTypeToCreate = new TicketType();

                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    ticketTypeToCreate.setEvent(eventToCreate);
                    return ticketTypeToCreate;
                }
        ).toList();


        eventToCreate.setName(event.getName());
        eventToCreate.setStart(event.getStart());
        eventToCreate.setEnd(event.getEnd());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSalesEnd(event.getSalesEnd());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setOrganizer(user);
        eventToCreate.setTicketTypes(ticketTypesToCreate);

        return eventRepository.save(eventToCreate);

    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
       return eventRepository.findByOrganizerId(organizerId, pageable);

    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID eventId) {
        return eventRepository.findByIdAndOrganizerId(eventId, organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID eventId,
                                         UpdateEventRequest event) {
        if(null == event.getId()){
            throw new EventUpdateException("Event id cannot be null");
        }

        if(!eventId.equals(event.getId())){
            throw new EventUpdateException("Event id in the path and request body must be the same");
        }

        Event existingEvent =
                eventRepository.findByIdAndOrganizerId(eventId, organizerId).orElseThrow(
                () -> new EventNotFoundException(
                        String.format("Event with id %s not found for organizer %s",
                                eventId, organizerId
                )
        ));

        existingEvent.setName(event.getName());
        existingEvent.setStart(event.getStart());
        existingEvent.setEnd(event.getEnd());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setSalesStart(event.getSalesStart());
        existingEvent.setSalesEnd(event.getSalesEnd());
        existingEvent.setStatus(event.getStatus());

        // UPDATE TICKET TYPES

        Set<UUID> requestTicketTypeIds = event.getTicketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::getId).
                filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Remove ticket types that are not in the request
        existingEvent.getTicketTypes().removeIf(
                ticketType -> !requestTicketTypeIds.contains(ticketType.getId())
        );

        // Map existing ticket types by id for easy lookup
        Map<UUID, TicketType> existingTicketTypeIndex = existingEvent.getTicketTypes().stream().collect(
                Collectors.toMap(
                        TicketType::getId,
                        Function.identity()
                )
        );

        for(UpdateTicketTypeRequest ticketTypeRequest : event.getTicketTypes()){
            if(null == ticketTypeRequest.getId()){
                // Add new ticket type
                TicketType newTicketType = new TicketType();
                newTicketType.setName(ticketTypeRequest.getName());
                newTicketType.setDescription(ticketTypeRequest.getDescription());
                newTicketType.setPrice(ticketTypeRequest.getPrice());
                newTicketType.setTotalAvailable(ticketTypeRequest.getTotalAvailable());
                newTicketType.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(newTicketType);

            }else if (existingTicketTypeIndex.containsKey(ticketTypeRequest.getId())) {
                // Update existing ticket type
                TicketType existingTicketType = existingTicketTypeIndex.get(ticketTypeRequest.getId());
                existingTicketType.setName(ticketTypeRequest.getName());
                existingTicketType.setDescription(ticketTypeRequest.getDescription());
                existingTicketType.setPrice(ticketTypeRequest.getPrice());
                existingTicketType.setTotalAvailable(ticketTypeRequest.getTotalAvailable());

            }
            else   {
                throw new TicketTypeNotFoundException(
                        String.format("Ticket type with id %s not found in the existing event",
                                ticketTypeRequest.getId())
                );
            }
        }

        return eventRepository.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEventForOrganizer(UUID organizerId, UUID eventId) {
         getEventForOrganizer(organizerId, eventId)
                 .ifPresent(eventRepository::delete);
    }

    @Override
    public Page<Event> listPublishedEvents(Pageable pageable) {
       return  eventRepository.findByStatus(EventStatusEnum.PUBLISHED,
               pageable);
    }

    @Override
    public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
        return eventRepository.searchEvents(query, pageable);
    }

    @Override
    public Optional<Event> getPublishedEvent(UUID eventId) {
        return eventRepository.findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED);
    }


}
