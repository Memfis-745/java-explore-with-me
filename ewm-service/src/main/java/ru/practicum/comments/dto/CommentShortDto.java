package ru.practicum.comments.dto;

import lombok.*;
import ru.practicum.event.dto.EventShortDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CommentShortDto {

    private Long id;

    private String text;

    private EventShortDto event;

    private LocalDateTime created;
}