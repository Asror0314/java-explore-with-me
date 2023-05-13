package ru.yandex.explore.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.category.dto.CategoryDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryController {
    private final CategoryService catService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto addNewCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return catService.addNewCategory(categoryDto);
    }

    @GetMapping("/catId")
    CategoryDto getCategoryById(@PathVariable Long catId) {
        return catService.getCategoryById(catId);
    }

    @GetMapping
    List<CategoryDto> getCategories(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return catService.getCategories(from, size);
    }

    @PatchMapping("/{catId}")
    CategoryDto updateCategory(
            @RequestBody CategoryDto categoryDto,
            @PathVariable(name = "catId") Long catId
    ) {
        return catService.updateCategory(categoryDto, catId);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable(name = "catId") Long catId) {
        catService.deleteCategory(catId);
    }
}
