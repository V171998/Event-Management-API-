package com.event.EventManagement.service;

import com.event.EventManagement.entity.Event;
import com.event.EventManagement.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    void deleteUserById(Long id);

    List<User> getAllUser();

    Set<Event> updateEventByUserId(Long eventId);

    Set<Event> getAllUserEvents();

    void deleteEventByUserById( Long eventId);

}
