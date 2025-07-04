package com.example.EventMate.Repository;


import com.example.EventMate.DTO.EventDTO;
import com.example.EventMate.Model.Event;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Event findByName(String name);

    List<Event> findByOrganiserUsername(String organiserUsername);

    List<Event> findEventByCategory(String category);
}