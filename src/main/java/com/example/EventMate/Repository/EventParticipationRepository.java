package com.example.EventMate.Repository;

import com.example.EventMate.Model.Event;
import com.example.EventMate.Model.EventParticipation;
import com.example.EventMate.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    boolean existsByUserAndEvent(User user, Event event);
    void deleteByUserAndEvent(User user, Event event);

    @Query("SELECT ep FROM EventParticipation ep JOIN FETCH ep.event WHERE ep.user = :user")
    List<EventParticipation> findByUserWithEvents(@Param("user") User user);

}
