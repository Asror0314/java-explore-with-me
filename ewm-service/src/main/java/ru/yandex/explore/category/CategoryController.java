package ru.yandex.explore.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.category.dto.CategoryDto;
import ru.yandex.explore.category.dto.NewCategoryDto;
import ru.yandex.explore.category.dto.UpdateCategoryDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService catService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto addNewCategory(@RequestBody @Valid NewCategoryDto categoryDto) {
        log.info("Add new category");
        return catService.addNewCategory(categoryDto);
    }

    @PatchMapping("/admin/categories/{catId}")
    CategoryDto updateCategory(
            @Valid @RequestBody UpdateCategoryDto categoryDto,
            @PathVariable(name = "catId") Long catId
    ) {
        log.info("Update category with catId = {}", catId);
        return catService.updateCategory(categoryDto, catId);

    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable(name = "catId") Long catId) {
        log.info("delete category with catId = {}", catId);
        catService.deleteCategory(catId);
    }

    @GetMapping("/categories/{catId}")
    CategoryDto getCategoryById(@PathVariable(name = "catId") Long catId) {
        log.info("Get category with catId = {}", catId);
        return catService.getCategoryById(catId);
    }

    @GetMapping("/categories")
    List<CategoryDto> getCategories(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Get category with from = {}, size = {}", from, size);
        return catService.getCategories(from, size);
    }

}
