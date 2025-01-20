package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryRepository.save(CategoryMapper.dtoToCategory(newCategoryDto));
        return CategoryMapper.categoryToDto(category);
    }

    @Override
    public void deleteCategory(long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Категория с ID: " + categoryId + " не найдена");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDto patchCategory(Long catId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID: " + catId + " не найдена"));
        Category categoryFromDto = CategoryMapper.dtoToCategory(categoryDto, category);
        categoryFromDto.setId(catId);
        return CategoryMapper.categoryToDto(categoryRepository.save(categoryFromDto));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);

        return categoryRepository.findAll(page).stream()
                .map(CategoryMapper::categoryToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategory(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID: " + catId + " не найдена"));
        return CategoryMapper.categoryToDto(category);
    }
}