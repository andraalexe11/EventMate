package com.example.EventMate.DTO;

import com.example.EventMate.Model.Event;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private String name;
    private String description;
    private String location;
    private LocalDateTime dateTime;
    private Integer max_attendants;
    private String organiser;

    public EventDTO(Event event) {
        this.name = event.getName();
        this.description = event.getDescription();
        this.location = event.getLocation();
        this.dateTime = event.getDateTime();
        this.max_attendants = event.getMax_attendants();
        this.organiser = event.getOrganiser().getUsername();
    }
}
