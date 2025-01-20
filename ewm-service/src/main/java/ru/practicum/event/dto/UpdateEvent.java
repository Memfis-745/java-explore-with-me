package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.location.dto.LocationDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateEvent {

    @Size(min = 20, max = 2000, message = "Краткое описание должно содержать не менее 20 и не более 2000 симоволов")
    private String annotation;

    @Positive
    private Long category;

    @Size(min = 20, max = 7000, message = "Полное описание описание должно содержать не менее 20 и не более 7000 симоволов")
    private String description;

    @Valid
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120, message = "Заголовок должн содержать не менее 3 и не более 120 симоволов")
    private String title;
}