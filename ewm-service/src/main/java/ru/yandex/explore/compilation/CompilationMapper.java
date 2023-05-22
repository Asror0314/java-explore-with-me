package ru.yandex.explore.compilation;

import ru.yandex.explore.compilation.dto.CompilationDto;
import ru.yandex.explore.compilation.dto.NewCompilationDto;
import ru.yandex.explore.compilation.dto.UpdateCompilationDto;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.event.EventMapper;
import ru.yandex.explore.event.dto.EventShortDto;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation mapNewCompilationDto2Compilation(NewCompilationDto compilationDto, List<Event> events) {
        final Compilation compilation = new Compilation();

        compilation.setEvents(events);
        compilation.setPinned(compilationDto.getPinned());
        compilation.setTitle(compilationDto.getTitle());
        return compilation;
    }

    public static Compilation mapUpdateCompilationDto2Compilation(UpdateCompilationDto compDto, List<Event> events) {
        final Compilation compilation = new Compilation();

        compilation.setTitle(compDto.getTitle());
        compilation.setEvents(events);
        compilation.setPinned(compDto.getPinned());
        return compilation;
    }

    public static CompilationDto mapCompilation2CompilationDto(Compilation compilation) {
        final CompilationDto compilationDto = new CompilationDto();
        final List<EventShortDto> eventShortDtos = compilation
                .getEvents()
                .stream()
                .map(EventMapper::mapEvent2EventShortDto)
                .collect(Collectors.toList());

        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(eventShortDtos);
        compilationDto.setPinned(compilation.getPinned());
        compilationDto.setTitle(compilation.getTitle());

        return compilationDto;
    }
}
