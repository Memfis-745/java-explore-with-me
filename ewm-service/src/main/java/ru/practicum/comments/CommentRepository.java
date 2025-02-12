package ru.practicum.comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comments.model.Comment;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByAuthorIdAndEventId(long userId, long eventId);

    Page<Comment> findAllByAuthorId(long userId, Pageable page);

    Page<Comment> findAllByEventId(long eventId, Pageable page);

    @Query("""
            select c.event.id as eventId, count(c.id) as commentsQuantity from
            Comment c where c.event.id in :eventsIds group by c.event.id
            """)
    List<Map<String, Long>> countCommentsForEvent(List<Long> eventsIds);
}