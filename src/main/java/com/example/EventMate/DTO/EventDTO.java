package com.example.EventMate.DTO;

import com.example.EventMate.Model.Event;
import com.example.EventMate.Model.EventParticipation;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Integer Id;
    private String name;
    private String description;
    private String location;
    private LocalDateTime dateTime;
    private Integer max_attendants;
    private String organiser;
    private String category;
    private List<String> participants;

    public EventDTO(Event event) {
        this.Id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.location = event.getLocation();
        this.dateTime = event.getDateTime();
        this.max_attendants = event.getMax_attendants();
        this.organiser = event.getOrganiser().getUsername();
        this.category = event.getCategory();
        this.participants = event.getParticipants() != null ?
                event.getParticipants().stream()
                        .map(participant -> participant.getUser().getUsername())
                        .collect(Collectors.toList()) :
                null;
    }

}
