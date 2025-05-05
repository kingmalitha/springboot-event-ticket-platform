package com.kingmalitha.springbooteventticketplatform.controllers;

import com.kingmalitha.springbooteventticketplatform.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.kingmalitha.springbooteventticketplatform.domain.dtos.ListPublishedEventResponseDto;
import com.kingmalitha.springbooteventticketplatform.domain.entities.Event;
import com.kingmalitha.springbooteventticketplatform.mappers.EventMapper;
import com.kingmalitha.springbooteventticketplatform.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping()
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents(
            @RequestParam(required = false) String query,
            Pageable pageable
    ){
        Page<Event> events;
        if(null != query && !query.trim().isEmpty()){
            events = eventService.searchPublishedEvents(query, pageable);
        } else {
            events = eventService.listPublishedEvents(pageable);
        }
      return ResponseEntity.ok(events.map(eventMapper::toListPublishedEventResponseDto));
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEventDetails(
            @PathVariable UUID eventId
    ){
        return eventService.getPublishedEvent(eventId)
                .map(eventMapper::toGetPublishedEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
