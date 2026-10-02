package com.event.EventManagement.service;

import com.event.EventManagement.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {

    Event createEvent (Event event);
    Page<Event> getAllEvent(Pageable pageable);
    Event getEventByID(long id);
    Event updateEvent(Event event , long id);
    void deleteEvent(long id);
}
