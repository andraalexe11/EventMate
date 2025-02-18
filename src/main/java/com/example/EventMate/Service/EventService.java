package com.example.EventMate.Service;

import com.example.EventMate.Exceptions.UserNotFoundException;
import com.example.EventMate.Model.Event;
import com.example.EventMate.Repository.EventRepository;
import com.example.EventMate.Exceptions.EventAlreadyExistsException;
import com.example.EventMate.Exceptions.EventNotFoundException;
import com.example.EventMate.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserService userService;

    /**
     * Retrieves all events from the repository.
     *
     * @return a list of all available events.
     */
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    /**
     * Adds a new event after verifying if an event with the same name already exists.
     *
     * @param event Event object that contains the information of the event to be added.
     * @return the added event.
     * @throws EventAlreadyExistsException if an event with the same name already exists.
     */
    public Event add(final Event event) throws EventAlreadyExistsException, UserNotFoundException {
        checkIfEventAlreadyExists(event.getName());
        return add(buildEvent(event));
    }

    /**
     * Checks if an event with a specific name exists in the repository.
     *
     * @param name the name of the event to be checked.
     * @throws EventAlreadyExistsException if the event already exists.
     */
    public void checkIfEventAlreadyExists(final String name) throws EventAlreadyExistsException {
        if (eventRepository.findByName(name).isPresent()) {
            throw new EventAlreadyExistsException("Event with this name already exists");
        }
    }

    /**
     * Builds an event object
     *
     * @param event the  object to build the event from.
     * @return the built event.
     */
    public Event buildEvent(final Event event) throws UserNotFoundException {
        var event1 = new Event();
        event1.setName(event.getName());
        event1.setDescription(event.getDescription());
        event1.setLocation(event.getLocation());
        event1.setDateTime(event.getDateTime());
        event1.setMax_attendants(event.getMax_attendants());

        // Set the organiser based on the ID
        User organiser = userService.findById(Math.toIntExact(event.getOrganiser().getId()));
        event1.setOrganiser(organiser);

        return event1;
    }



    /**
     * Updates an event with the specified name.
     *
     * @param oldName the name of the event to be updated.
     * @param event the object containing the new information for the event.
     * @return the updated event.
     * @throws EventNotFoundException if the event is not found.
     */
    public Event update(final String oldName, final Event event) throws EventNotFoundException, UserNotFoundException {
        return updateEvent(findEvent(oldName), event);
    }

    /**
     * Finds an event by its name.
     *
     * @param name the name of the event to be searched.
     * @return the found event.
     * @throws EventNotFoundException if no event with the specified name is found.
     */
    public Event findEvent(final String name) throws EventNotFoundException {
        Optional<Event> event = eventRepository.findByName(name);
        if (event.isEmpty()) {
            throw new EventNotFoundException("Event not found");
        }
        return event.get();
    }

    /**
     * Updates the event with the specified name.
     *
     * @param event the event to be updated.
     * @param event the  object containing the new information for the event.
     * @return the updated event.
     */
    public Event updateEvent(final Event eventnew, final Event event) throws UserNotFoundException {
        eventnew.setName(event.getName());
        eventnew.setDescription(event.getDescription());
        eventnew.setLocation(event.getLocation());
        eventnew.setDateTime(event.getDateTime());
        eventnew.setMax_attendants(event.getMax_attendants());

        // Update organiser if necessary
        User organiser = userService.findById(Math.toIntExact(event.getOrganiser().getId()));
        event.setOrganiser(organiser);

        return eventRepository.save(event);
    }

    /**
     * Deletes an event from the repository based on its name.
     *
     * @param name the name of the event to delete.
     * @throws EventNotFoundException if no event with the specified name is found.
     */
    public void delete(final String name) throws EventNotFoundException {
        findEvent(name);
        eventRepository.delete(findEvent(name));
    }


}
