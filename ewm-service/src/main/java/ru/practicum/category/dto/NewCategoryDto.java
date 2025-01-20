package ru.practicum.category.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NewCategoryDto {

    @NotBlank(message = "Имя категории обязательно для заполнения")
    @Size(min = 1, max = 50, message = "Имя должно содержать не менее 1 и не более 50 симоволов")
    private String name;
}