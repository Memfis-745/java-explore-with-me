package ru.practicum.category;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

@UtilityClass
public class CategoryMapper {

    public CategoryDto categoryToDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public Category dtoToCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                0L,
                newCategoryDto.getName()
        );
    }

    public Category dtoToCategory(CategoryDto categoryDto, Category category) {
        return new Category(
                categoryDto.getId(),
                categoryDto.getName() != null ? categoryDto.getName() : category.getName()
        );
    }
}