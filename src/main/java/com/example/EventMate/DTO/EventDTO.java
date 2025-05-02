package com.example.EventMate.DTO;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDateTime;

public class EventDTO {
    private String name;
    private String description;
    private String location;
    private LocalDateTime dateTime;
    private Integer max_attendants;

}
