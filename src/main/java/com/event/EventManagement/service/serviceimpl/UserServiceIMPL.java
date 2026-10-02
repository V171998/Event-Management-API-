package com.event.EventManagement.service.serviceimpl;

import com.event.EventManagement.entity.Event;
import com.event.EventManagement.entity.User;
import com.event.EventManagement.repository.UserRepository;
import com.event.EventManagement.service.EventService;
import com.event.EventManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceIMPL implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EventService eventService;

    public User getLoggedInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User Not Found!!"));
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail()) ){

            throw new RuntimeException("EMAIL ID ALREADY EXISTS!!!");
        }
        user.setRoles(Arrays.asList("USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("USER NOT FOUND!!"));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Set<Event> updateEventByUserId( Long eventId) {
        Set<Event> allEvents;
        User u = getLoggedInUser();
        Event e = eventService.getEventByID(eventId);
        allEvents = u.getEvents();
        allEvents.add(e);
        u.setEvents(allEvents);
        userRepository.save(u);
        return u.getEvents();
    }

    @Override
    public Set<Event> getAllUserEvents() {
            User u = getLoggedInUser();
        return u.getEvents();
    }

    @Override
    public void deleteEventByUserById( Long eventId) {

        Set<Event> allEvents;
        User u = getLoggedInUser();
        allEvents=u.getEvents();
        Set<Event> events = allEvents.stream().filter(e->e.getId()!=eventId).collect(Collectors.toSet());
        u.setEvents(events);
        userRepository.save(u);
    }
}
