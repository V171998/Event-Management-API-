package com.event.EventManagement.controller;

import com.event.EventManagement.entity.Event;
import com.event.EventManagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvent(Pageable page){

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvent(page).toList());

        //return new ResponseEntity<> (eventService.getAllEvent() , HttpStatus.ok);

    };

    @PostMapping()
    public ResponseEntity<Event> createEvent(@RequestBody Event event){

        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(event));
    };

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventByID(@PathVariable long id){

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventByID(id));
    };

    @PutMapping("{id}")
    public ResponseEntity<Event> updateEvent(@RequestBody Event e , @PathVariable long id){

        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEvent(e,id));
    };

    @DeleteMapping
    public ResponseEntity<?> deleteEvent(@RequestParam long id){

       eventService.deleteEvent(id);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    };


}
