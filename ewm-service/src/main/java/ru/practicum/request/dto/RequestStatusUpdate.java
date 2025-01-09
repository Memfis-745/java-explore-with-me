package ru.practicum.request.dto;

import lombok.*;
import ru.practicum.validation.StatusConstrain;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestStatusUpdate {

    @NotNull
    private List<Long> requestIds;

    @StatusConstrain
    @NotNull
    private String status;
}