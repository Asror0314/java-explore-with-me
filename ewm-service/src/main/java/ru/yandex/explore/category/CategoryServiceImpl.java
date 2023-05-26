package ru.yandex.explore.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.explore.category.dto.CategoryDto;
import ru.yandex.explore.category.dto.NewCategoryDto;
import ru.yandex.explore.category.dto.UpdateCategoryDto;
import ru.yandex.explore.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository catRepository;

    @Override
    @Transactional
    public CategoryDto addNewCategory(NewCategoryDto categoryDto) {
        final Category category = CategoryMapper.mapNewCategoryDtoToCategory(categoryDto);
        final Category addedCategory = catRepository.save(category);
        return CategoryMapper.mapCategoryToCategoryDto(addedCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(UpdateCategoryDto categoryDto, Long catId) {
        getCategoryById(catId);
        final Category category = CategoryMapper.mapUpdateCategoryDtoToCategory(categoryDto, catId);

        final Category updatedCategory = catRepository.save(category);
        return CategoryMapper.mapCategoryToCategoryDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        catRepository.findById(catId);
        catRepository.deleteById(catId);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        final Category category = catRepository.findById(catId).orElseThrow(
                () -> new NotFoundException(String.format("category id = %d not found", catId)));

        return CategoryMapper.mapCategoryToCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return catRepository
                .findAll(page)
                .stream()
                .map(CategoryMapper::mapCategoryToCategoryDto)
                .collect(Collectors.toList());
    }

}
