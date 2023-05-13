package ru.yandex.explore.category;

import ru.yandex.explore.category.dto.CategoryDto;

public class CategoryMapper {
    public static Category map2Category(CategoryDto categoryDto) {
        final Category category = new Category();

        category.setName(categoryDto.getName());
        return category;
    }

    public static CategoryDto map2CategoryDto(Category category) {
        final CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
