package com.example.EventMate.Controller;
import com.example.EventMate.DTO.EventDTO;
import com.example.EventMate.Exceptions.EventNotFoundException;
import com.example.EventMate.Exceptions.UserNotFoundException;
import com.example.EventMate.Service.EventService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.EventMate.Model.Event;
import com.example.EventMate.Model.User;
import com.example.EventMate.Repository.UserRepository;
import com.example.EventMate.Service.EventParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participation")
@RequiredArgsConstructor
public class EventParticipationController {
    private final EventParticipationService participationService;
    private UserRepository userRepository;
    private EventService eventService;

    @Autowired
    public EventParticipationController(EventParticipationService participationService, UserRepository userRepository, EventService eventService) {
        this.participationService = participationService;
        this.userRepository = userRepository;
        this.eventService = eventService;
    }

    @PostMapping("/join")
    @PreAuthorize("isAuthenticated()")

    public ResponseEntity<String> joinEvent(@RequestParam String title, @RequestParam String participant) throws UserNotFoundException, EventNotFoundException {
        User user  = userRepository.findByUsername(participant)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + participant));
        Event event  = eventService.findEvent(title);
        long eventId = event.getId();
        participationService.joinEvent(user, Math.toIntExact(eventId));
        return ResponseEntity.ok("Joined successfully!");
    }



    @DeleteMapping("/leave")
    @Transactional
    public ResponseEntity<String> leaveEvent(@RequestParam String title, @RequestParam String username) throws UserNotFoundException, EventNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventService.findEvent(title);
        int eventId = event.getId();
        participationService.leaveEvent(user, eventId);
        return ResponseEntity.ok("Left the event successfully!");
    }

    @GetMapping("/my-events")
    public ResponseEntity<List<EventDTO>> getUserEvents(@RequestParam String username) throws UserNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<EventDTO> events = participationService.getUserEvents(user);
        return ResponseEntity.ok(events);
    }

}

