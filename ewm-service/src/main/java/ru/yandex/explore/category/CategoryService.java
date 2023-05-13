package ru.yandex.explore.category;

import ru.yandex.explore.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addNewCategory(CategoryDto categoryDto);

    CategoryDto getCategoryById(Long catId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto updateCategory(CategoryDto categoryDto, Long catId);

    void deleteCategory(Long catId);
}
