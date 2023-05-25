package ru.yandex.explore.category;

import ru.yandex.explore.category.dto.CategoryDto;
import ru.yandex.explore.category.dto.NewCategoryDto;
import ru.yandex.explore.category.dto.UpdateCategoryDto;

public class CategoryMapper {
    public static Category mapNewCategoryDtoToCategory(NewCategoryDto categoryDto) {
        final Category category = new Category();

        category.setName(categoryDto.getName());
        return category;
    }

    public static Category mapUpdateCategoryDtoToCategory(UpdateCategoryDto categoryDto, Long catId) {
        final Category category = new Category();

        category.setId(catId);
        category.setName(categoryDto.getName());
        return category;
    }

    public static CategoryDto mapCategoryToCategoryDto(Category category) {
        final CategoryDto categoryDto = new CategoryDto();

        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}
