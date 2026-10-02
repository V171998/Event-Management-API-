package com.event.EventManagement.controller;

import com.event.EventManagement.entity.Event;
import com.event.EventManagement.entity.User;
import com.event.EventManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public User createUser(@RequestBody User user){

        return userService.createUser(user);

    };

    @GetMapping("/{id}")
    public User getUserByID(@PathVariable long id){

        return userService.getUserById(id);
    };

    @GetMapping()
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id ){
        userService.deleteUserById(id);
    }

    @PutMapping("/event/{eventId}")
    public Set<Event>updateEventByUserId(@PathVariable Long eventId){
        return userService.updateEventByUserId(eventId);

    }

    @GetMapping("/event")
    public Set<Event> getAllEventsByUser(){
        return userService.getAllUserEvents();
    }

    @DeleteMapping("/event/{eventId}")
    public void deleteEventByUserById(@PathVariable Long eventId){
        userService.deleteEventByUserById(eventId);
    }

}
