package ru.yandex.explore.category;

import ru.yandex.explore.category.dto.CategoryDto;
import ru.yandex.explore.category.dto.NewCategoryDto;

public class CategoryMapper {
    public static Category mapNewCategoryDto2Category(NewCategoryDto categoryDto) {
        final Category category = new Category();

        category.setName(categoryDto.getName());
        return category;
    }

    public static CategoryDto mapCategory2CategoryDto(Category category) {
        final CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
