package ru.practicum.compilation.dto;

import lombok.*;

import jakarta.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateCompilationRequest {

    @Size(min = 1, max = 50, message = "Наименование подборки содержать не менее 1 и не более 50 симоволов")
    private String title;

    private Boolean pinned;

    private Set<Long> events;

}