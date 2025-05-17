package com.example.EventMate.Service;

import com.example.EventMate.DTO.EventDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
        if (event.getMax_attendants() != null
                && event.getParticipants().size() >= event.getMax_attendants()) {
            throw new RuntimeException("Event is full!");
        }

        EventParticipation participation = new EventParticipation();
        participation.setUser(user);
        participation.setEvent(event);
        participation.setJoinedAt(LocalDateTime.now());
        participationRepository.save(participation);
        Set<EventParticipation> participationsEvent = event.getParticipants();
        participationsEvent.add(participation);
        event.setParticipants(participationsEvent);
        Set<EventParticipation> participationsUser = user.getEventParticipations();
        participationsUser.add(participation);
        user.setEventParticipations(participationsUser);

    }

    public void leaveEvent(User user, int eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!participationRepository.existsByUserAndEvent(user, event)) {
            throw new RuntimeException("You are not part of this event");
        }
        Set<EventParticipation> participations = event.getParticipants();
        participationRepository.deleteByUserAndEvent(user, event);
        participations.removeIf(participation -> Objects.equals(participation.getUser().getId(), user.getId()));
        Set<EventParticipation> participationsUser = user.getEventParticipations();
        participationsUser.removeIf(participation -> Objects.equals(participation.getEvent().getId(), eventId));
        user.setEventParticipations(participationsUser);
        event.setParticipants(participations);

    }

    public List<EventDTO> getUserEvents(User user) {
            return participationRepository.findByUserWithEvents(user).stream()
                    .map(EventParticipation::getEvent)
                    .map(EventDTO::new) // Transformi în DTO
                    .collect(Collectors.toList());



    }
}
