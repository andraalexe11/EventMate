package com.example.EventMate.Controller;
import com.example.EventMate.DTO.EventDTO;
import com.example.EventMate.Exceptions.EventAlreadyExistsException;
import com.example.EventMate.Exceptions.EventNotFoundException;
import com.example.EventMate.Exceptions.UserNotFoundException;
import com.example.EventMate.Model.Event;
import com.example.EventMate.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<EventDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * Retrieves an event by its ID.
     *
     * @param name the name of the event.
     * @return the Event for the specified name.
     * @throws EventNotFoundException if the event is not found.
     */
    @GetMapping("/findbyname")
    public ResponseEntity<EventDTO> getEventByName(@RequestParam String name) throws EventNotFoundException {
        EventDTO event = eventService.findEvent(name);
        return ResponseEntity.ok(event);
    }
    @GetMapping("/findbyorganiser")
    public ResponseEntity<List<EventDTO>> getEventbyUsername(@RequestParam String organiser) throws EventNotFoundException {
        List<EventDTO> events = eventService.findbyUsername(organiser);
        return ResponseEntity.ok(events);
    }


    @GetMapping("/findbycategory")
    public ResponseEntity<List<EventDTO>> getEventbyCategory(@RequestParam String category) throws EventNotFoundException {
        List<EventDTO> events = eventService.getEventsByCategory(category);
        return ResponseEntity.ok(events);
    }

    /**
     * Creates a new event.
     *
     * @param event the Event containing the details of the event to be created.
     * @return the created Event
     */
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody EventDTO event) throws UserNotFoundException, EventAlreadyExistsException {
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
    @PutMapping("/update")
    public ResponseEntity<EventDTO> updateEvent(@RequestParam String name, @RequestBody EventDTO event) throws EventNotFoundException, UserNotFoundException {
        EventDTO updatedEvent = eventService.update(name, event);
        return ResponseEntity.ok(updatedEvent);
    }

    /**
     * Deletes an event by its name.
     *
     * @param name the name of the event to be deleted.
     * @throws EventNotFoundException if the event is not found.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteEvent(@RequestParam  String name) throws EventNotFoundException {
        eventService.delete(name);
        return ResponseEntity.noContent().build();
    }
}
