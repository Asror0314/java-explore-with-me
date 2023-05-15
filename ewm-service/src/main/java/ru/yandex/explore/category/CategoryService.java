package ru.yandex.explore.category;

import ru.yandex.explore.category.dto.CategoryDto;
import ru.yandex.explore.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addNewCategory(NewCategoryDto categoryDto);

    CategoryDto getCategoryById(Long catId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto updateCategory(NewCategoryDto categoryDto, Long catId);

    void deleteCategory(Long catId);
}
