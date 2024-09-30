package com.PCA.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getByName(String name) {
        return eventRepository.findByName(name);
    }

    public boolean save(Event event) {
        if (event.getName() == null || event.getName().isEmpty()) {
            return false;
        } else {
            eventRepository.save(event);
            return true;
        }
    }

    public List<Event> findAllList() {
        return eventRepository.findAll();
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public Event findByName(String name) {
        return eventRepository.findById(name).get();
    }
}

