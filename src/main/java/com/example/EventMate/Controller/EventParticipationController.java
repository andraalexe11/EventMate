package com.example.EventMate.Controller;
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

    @Autowired
    public EventParticipationController(EventParticipationService participationService, UserRepository userRepository) {
        this.participationService = participationService;
        this.userRepository = userRepository;
    }

    @PostMapping("/join/{eventId}")
    @PreAuthorize("isAuthenticated()")

    public ResponseEntity<String> joinEvent(@PathVariable Long eventId, @RequestParam Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));
        participationService.joinEvent(user, Math.toIntExact(eventId));
        return ResponseEntity.ok("Joined successfully!");
    }



    @DeleteMapping("/leave/{eventId}")
    @Transactional
    public ResponseEntity<String> leaveEvent(@PathVariable int eventId, @RequestParam Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        participationService.leaveEvent(user, eventId);
        return ResponseEntity.ok("Left the event successfully!");
    }

    @GetMapping("/my-events")
    public ResponseEntity<List<Event>> getUserEvents(@RequestParam Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Event> events = participationService.getUserEvents(user);
        return ResponseEntity.ok(events);
    }

}

