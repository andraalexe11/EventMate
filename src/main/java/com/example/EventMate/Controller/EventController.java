package com.example.EventMate.Controller;
import com.example.EventMate.Exceptions.EventAlreadyExistsException;
import com.example.EventMate.Exceptions.EventNotFoundException;
import com.example.EventMate.Exceptions.UserNotFoundException;
import com.example.EventMate.Model.Event;
import com.example.EventMate.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * Retrieves all events.
     *
     * @return a list of EventDTOs.
     */
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAll();
        return ResponseEntity.ok(events);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param name the name of the event.
     * @return the Event for the specified name.
     * @throws EventNotFoundException if the event is not found.
     */
    @GetMapping("/{name}")
    public ResponseEntity<Event> getEventById(@PathVariable String name) throws EventNotFoundException {
        Event event = eventService.findEvent(name);
        return ResponseEntity.ok(event);
    }

    /**
     * Creates a new event.
     *
     * @param event the Event containing the details of the event to be created.
     * @return the created Event
     */
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) throws UserNotFoundException, EventAlreadyExistsException {
        Event createdEvent = eventService.add(event);
        return ResponseEntity.ok(createdEvent);
    }

    /**
     * Updates an existing event.
     *
     * @param name the name of the event to be updated.
     * @param event the Event containing the updated details.
     * @return the updated EventDTO.
     * @throws EventNotFoundException if the event is not found.
     */
    @PutMapping("/{name}")
    public ResponseEntity<Event> updateEvent(@PathVariable String name, @RequestBody Event event) throws EventNotFoundException, UserNotFoundException {
        Event updatedEvent = eventService.update(name, event);
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Deletes an event by its name.
     *
     * @param name the name of the event to be deleted.
     * @throws EventNotFoundException if the event is not found.
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String name) throws EventNotFoundException {
        eventService.delete(name);
        return ResponseEntity.noContent().build();
    }
}
