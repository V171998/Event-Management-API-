package com.event.EventManagement.service.serviceimpl;

import com.event.EventManagement.entity.Event;
import com.event.EventManagement.repository.EventRepository;
import com.event.EventManagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceIMPL implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Page<Event> getAllEvent(Pageable page) {
        return eventRepository.findAll(page);
    }

    @Override
    public Event getEventByID(long id) {
        return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("EVENT Not found"));
    }

    @Override
    public Event updateEvent(Event event, long id) {

       Event e = getEventByID(id);
       e.setName(event.getName()!=null?event.getName():e.getName());
       e.setDescription(event.getDescription()!=null?event.getDescription():e.getDescription());
       e.setFee(event.getFee()!=null ?event.getFee():e.getFee());
       e.setStartDate(event.getStartDate()!= null?event.getStartDate():e.getStartDate());
       e.setEndDate(event.getEndDate()!=null?event.getEndDate():e.getEndDate());
        eventRepository.save(e);
        return e ;
    }

    @Override
    public void deleteEvent(long id) {

        Event e = getEventByID(id);
        eventRepository.delete(e);
    }
}
