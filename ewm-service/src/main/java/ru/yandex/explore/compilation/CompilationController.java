package ru.yandex.explore.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.explore.compilation.dto.CompilationDto;
import ru.yandex.explore.compilation.dto.NewCompilationDto;
import ru.yandex.explore.compilation.dto.UpdateCompilationDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationController {
    private final CompilationService service;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addNewCompilation(
            @Valid @RequestBody NewCompilationDto compDto
            ) {
        log.info("Creating new compilation");
        return service.addNewCompilation(compDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(
            @Valid @RequestBody UpdateCompilationDto compDto,
            @Positive @PathVariable(name = "compId") Long compId
            ) {
        log.info("Creating new compilation with compId={}", compId);
        return service.updateCompilation(compDto, compId);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@Positive @PathVariable(name = "compId") Long compId) {
        log.info("Get compilation with id={}", compId);
        return service.getCompilationById(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Get compilations with pinned={}, from={}, size={}", pinned, from, size);
        return service.getCompilations(pinned, from, size);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable(name = "compId") Long compId) {
        log.info("Delete compilation with id={}", compId);
        service.deleteCompilationById(compId);
    }

}
