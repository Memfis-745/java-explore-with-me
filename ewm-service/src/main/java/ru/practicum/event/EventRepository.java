package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(Long userId, Pageable page);

    @Query("""
            select event FROM Event event WHERE (:users IS NULL
            OR event.initiator.id IN :users) AND (:states IS NULL
            OR event.state IN :states) AND (:categories IS NULL
            OR event.category.id IN :categories) AND (event.eventDate >= :rangeStart)
            AND (event.eventDate <= :rangeEnd)"""
    )
    Page<Event> getAllEventParams(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd, Pageable page);

    @Query("""
            select event FROM Event event WHERE event.state= :state
            AND (:text IS NULL OR (LOWER(event.description) LIKE %:text%
            OR LOWER(event.annotation) LIKE %:text%)) AND (:paid IS NULL
            OR event.paid = :paid) AND (event.eventDate >= :rangeStart) AND (event.eventDate <= :rangeEnd)
            ORDER BY event.eventDate"""
    )
    Page<Event> getPublicEventsWithFilter(
            State state,
            String text,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd, Pageable page);

    @Query("""
            select event FROM Event event WHERE event.state= :state
            AND (:text IS NULL OR (LOWER(event.description) LIKE %:text%
            OR LOWER(event.annotation) LIKE %:text%)) AND (:paid IS NULL
            OR event.paid = :paid) AND (event.eventDate >= :rangeStart)
            ORDER BY event.eventDate"""
    )
    Page<Event> getPublicEventsWithDateNull(
            State state,
            String text,
            Boolean paid,
            LocalDateTime rangeStart, Pageable page);

    @Query("select min(e.publishedOn) from Event as e where e.id in ?1")
    Optional<LocalDateTime> getMinPublishedDate(List<Long> eventsId);

}