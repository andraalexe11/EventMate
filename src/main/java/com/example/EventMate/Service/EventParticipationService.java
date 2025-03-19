package com.example.EventMate.Service;

import com.example.EventMate.Model.Event;
import com.example.EventMate.Model.EventParticipation;
import com.example.EventMate.Model.User;
import com.example.EventMate.Repository.EventParticipationRepository;
import com.example.EventMate.Repository.EventRepository;
import com.example.EventMate.Repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventParticipationService {
    private final EventParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public void joinEvent(User user, int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (participationRepository.existsByUserAndEvent(user, event)) {
            throw new RuntimeException("Already joined this event");
        }

        EventParticipation participation = new EventParticipation();
        participation.setUser(user);
        participation.setEvent(event);
        participation.setJoinedAt(LocalDateTime.now());
        participationRepository.save(participation);
    }

    public void leaveEvent(User user, int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!participationRepository.existsByUserAndEvent(user, event)) {
            throw new RuntimeException("You are not part of this event");
        }

        participationRepository.deleteByUserAndEvent(user, event);
    }

    public List<Event> getUserEvents(User user) {
        return participationRepository.findByUser(user).stream()
                .map(EventParticipation::getEvent)
                .collect(Collectors.toList());
    }
}
