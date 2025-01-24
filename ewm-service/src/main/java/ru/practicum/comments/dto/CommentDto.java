package ru.practicum.comments.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentDto {

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 10, max = 2500, message = "Текст комментария должен содержать не менее 10 симоволов")
    private String text;
}