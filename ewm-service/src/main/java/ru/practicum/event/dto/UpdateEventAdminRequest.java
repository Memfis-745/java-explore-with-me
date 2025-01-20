package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.validation.AdminEventDateConstrain;
import ru.practicum.validation.AdminStateActionConstrain;

import jakarta.validation.constraints.Pattern;

import static ru.practicum.exceptions.Constants.DATE_TIME_PATTERN;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UpdateEventAdminRequest extends UpdateEvent {

    @Pattern(regexp = DATE_TIME_PATTERN,
            message = "Формат даты и времени должен соответствовать виду: 'yyyy-MM-dd HH:mm:ss'")
    @AdminEventDateConstrain
    private String eventDate;

    @AdminStateActionConstrain
    private String stateAction;

}