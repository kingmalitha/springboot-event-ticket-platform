package com.kingmalitha.springbooteventticketplatform.controllers;

import com.kingmalitha.springbooteventticketplatform.domain.CreateEventRequest;
import com.kingmalitha.springbooteventticketplatform.domain.UpdateEventRequest;
import com.kingmalitha.springbooteventticketplatform.domain.dtos.*;
import com.kingmalitha.springbooteventticketplatform.domain.entities.Event;
import com.kingmalitha.springbooteventticketplatform.mappers.EventMapper;
import com.kingmalitha.springbooteventticketplatform.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.kingmalitha.springbooteventticketplatform.util.JwtUtil.parseUserId;

@RestController
@RequestMapping(path = "api/v1/events")
@RequiredArgsConstructor
public class EventController {


    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto createEventRequestDto
    ) {
        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);

        UUID userId = parseUserId(jwt);

        Event createdEvent = eventService.createEvent(userId,
                createEventRequest);

        return new ResponseEntity<>(eventMapper.toDto(createdEvent), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
    ){
        UUID userId = parseUserId(jwt);

        Page<Event> eventsPage = eventService.listEventsForOrganizer(userId, pageable);

        return new ResponseEntity<>(eventsPage.map(
                eventMapper::toListEventResponseDto), HttpStatus.OK);
    }


    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto> getEventDetails(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId
    ){
        UUID userId = parseUserId(jwt);

        return  eventService
                .getEventForOrganizer(userId, eventId)
                .map(eventMapper:: toGetEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{eventId}")
    public ResponseEntity<UpdateEventResponseDto> updateEventDetails(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto
    ){
        UUID userId = parseUserId(jwt);
        UpdateEventRequest updateEventRequest =
                eventMapper.fromDto(updateEventRequestDto);

        Event updatedEvent = eventService
                .updateEventForOrganizer(userId, eventId, updateEventRequest);

        return ResponseEntity.ok(
                eventMapper.toUpdateEventResponseDto(updatedEvent)
        );
    }

    @DeleteMapping(path = "/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId
    ){
        UUID userId = parseUserId(jwt);
        eventService.deleteEventForOrganizer(userId, eventId);
        return ResponseEntity.noContent().build();
    }





}
