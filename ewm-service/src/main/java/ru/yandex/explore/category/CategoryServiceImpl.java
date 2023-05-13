package ru.yandex.explore.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.yandex.explore.category.dto.CategoryDto;
import ru.yandex.explore.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository catRepository;

    @Override
    public CategoryDto addNewCategory(CategoryDto categoryDto) {
        final Category category = CategoryMapper.map2Category(categoryDto);
        final Category addedCategory = catRepository.save(category);

        return CategoryMapper.map2CategoryDto(addedCategory);
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        final Category category = catRepository.findById(catId).orElseThrow(
                () -> new NotFoundException(String.format("category id = %d not found", catId))
        );

        return CategoryMapper.map2CategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest page = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return catRepository
                .findAll(page)
                .stream()
                .map(CategoryMapper::map2CategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long catId) {
        getCategoryById(catId);
        final Category category = CategoryMapper.map2Category(categoryDto);
        final Category updatedCategory = catRepository.save(category);

        return CategoryMapper.map2CategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long catId) {
        catRepository.findById(catId);
        catRepository.deleteById(catId);
    }
}
