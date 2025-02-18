package com.example.EventMate.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Event {
    @Id
    @NotNull
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @NotBlank(message = "Event name cannot be empty")
    private String name;

    @NotBlank(message = "Event description cannot be empty")
    private String description;

    @NotBlank(message = "Event location cannot be empty")
    private String location;

    @NotNull(message = "Data and time cannot be null")
    private LocalDateTime dateTime;


    private Integer max_attendants;
    //preferinte
    @ManyToOne
    @JoinColumn(name = "organiser_id")
    private User organiser = new User();

}
