package com.example.EventMate.Service;

import com.example.EventMate.DTO.EventDTO;
import com.example.EventMate.Exceptions.UserNotFoundException;
import com.example.EventMate.Model.Event;
import com.example.EventMate.Repository.EventRepository;
import com.example.EventMate.Exceptions.EventAlreadyExistsException;
import com.example.EventMate.Exceptions.EventNotFoundException;
import com.example.EventMate.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.EventMate.Model.User;
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
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList(); // Mapăm entitățile la DTO-uri
    }

    private EventDTO convertToDTO(Event event) {
       EventDTO eventDTO = new EventDTO();
       eventDTO.setName(event.getName());
       eventDTO.setDescription(event.getDescription());
       eventDTO.setLocation(event.getLocation());
       eventDTO.setDateTime(event.getDateTime());
       eventDTO.setMax_attendants(event.getMax_attendants());
       eventDTO.setOrganiser(event.getOrganiser().getUsername());
       return eventDTO;
    }


    /**
     * Adds a new event after verifying if an event with the same name already exists.
     *
     * @param event Event object that contains the information of the event to be added.
     * @return the added event.
     * @throws EventAlreadyExistsException if an event with the same name already exists.
     */
    public Event add(final EventDTO event) throws EventAlreadyExistsException, UserNotFoundException {
        checkIfEventAlreadyExists(event.getName());
        User organiser = userService.findByUsername(event.getOrganiser());

        var event1 = new Event();
        event1.setName(event.getName());
        event1.setDescription(event.getDescription());
        event1.setLocation(event.getLocation());
        event1.setDateTime(event.getDateTime());
        event1.setMax_attendants(event.getMax_attendants());
        event1.setOrganiser(organiser);
        event1.setParticipants(null);
        eventRepository.save(event1);
        return event1;
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
     * Updates an event with the specified name.
     *
     * @param oldName the name of the event to be updated.
     * @param event the object containing the new information for the event.
     * @return the updated event.
     * @throws EventNotFoundException if the event is not found.
     */
    public Event update(final String oldName, final EventDTO event) throws EventNotFoundException, UserNotFoundException {
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
     * @param eventnew the event to be updated.
     * @param event the  object containing the new information for the event.
     * @return the updated event.
     */
    public Event updateEvent(final Event eventnew, final EventDTO event) throws UserNotFoundException {
        User organiser = userService.findByUsername(event.getOrganiser());

        eventnew.setName(event.getName());
        eventnew.setDescription(event.getDescription());
        eventnew.setLocation(event.getLocation());
        eventnew.setDateTime(event.getDateTime());
        eventnew.setMax_attendants(event.getMax_attendants());
        eventnew.setOrganiser(organiser);
        eventnew.setParticipants(null);
        return eventRepository.save(eventnew);
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

    public Optional<Event> findbyID(Integer id) throws EventNotFoundException{
        return eventRepository.findById(id);
    }

    public List<Event> findbyUsername(String username) throws EventNotFoundException{

        return eventRepository.findByOrganiserUsername(username);
    }

}
