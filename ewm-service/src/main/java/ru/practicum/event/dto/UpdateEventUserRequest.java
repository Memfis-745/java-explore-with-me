package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.validation.EventDateConstrain;
import ru.practicum.validation.UserStateActionConstrain;

import jakarta.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UpdateEventUserRequest extends UpdateEvent {

    @Pattern(regexp = "^[0-9]{4}-(0[1-9]|1[012])-(0[1-9]|1[0-9]|2[0-9]|3[01]) (([0,1][0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]$",
            message = "Формат даты и времени должен соответствовать виду: 'yyyy-MM-dd HH:mm:ss'")
    @EventDateConstrain
    private String eventDate;

    @UserStateActionConstrain
    private String stateAction;

}