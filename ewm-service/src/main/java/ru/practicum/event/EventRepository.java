package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
            OR event.category.id IN :categories) AND (:rangeStart
            IS NULL OR event.eventDate >= :rangeStart) AND (:rangeEnd IS
            NULL OR event.eventDate <= :rangeEnd)"""
    )
    List<Event> getAllEventParams(
            @Param("users") List<Long> users,
            @Param("states") List<String> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd);


   @Query("""
            select event FROM Event event WHERE event.state= :state
            AND (:text IS NULL OR (LOWER(event.description) LIKE %:text%
            OR LOWER(event.annotation) LIKE %:text%)) AND (:paid IS NULL
            OR event.paid = :paid) AND (:rangeStart IS NULL
            OR event.eventDate >= :rangeStart) AND (:rangeEnd IS NULL
            OR event.eventDate <= :rangeEnd) ORDER BY event.eventDate"""
    )

    //List<Event> getPublicEventsWithFilter(
           Page<Event> getPublicEventsWithFilter(
                   @Param("state") State state,
                   @Param("text") String text,
                   @Param("paid") Boolean paid,
                  // @Type
                   @Param("rangeStart") LocalDateTime rangeStart,
                   @Param("rangeEnd") LocalDateTime rangeEnd, Pageable page);


    @Query("select min(e.publishedOn) from Event as e where e.id in ?1")
    Optional<LocalDateTime> getMinPublishedDate(List<Long> eventsId);

}