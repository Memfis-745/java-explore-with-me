package ru.practicum.location.dto;

import lombok.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LocationDto {

    @Min(value = -90)
    @Max(value = 90)
    @NotNull
    private double lat;

    @Min(value = -180)
    @Max(value = 180)
    @NotNull
    private double lon;
}