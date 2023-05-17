package ru.yandex.explore.compilation;

import ru.yandex.explore.compilation.dto.CompilationDto;
import ru.yandex.explore.compilation.dto.NewCompilationDto;
import ru.yandex.explore.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto addNewCompilation(NewCompilationDto compDto);

    CompilationDto updateCompilation(UpdateCompilationDto compRequest, Long compId);

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    void deleteCompilationById(Long compId);
}
