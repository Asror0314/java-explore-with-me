package ru.yandex.explore.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.explore.compilation.dto.CompilationDto;
import ru.yandex.explore.compilation.dto.NewCompilationDto;
import ru.yandex.explore.compilation.dto.UpdateCompilationDto;
import ru.yandex.explore.event.Event;
import ru.yandex.explore.event.EventRepository;
import ru.yandex.explore.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addNewCompilation(NewCompilationDto compDto) {
        final List<Event> events = eventRepository.findAllById(compDto.getEvents());
        final Compilation compilation = CompilationMapper.mapNewCompilationDto2Compilation(compDto, events);
        final Compilation addedCompilation = compRepository.save(compilation);

        return CompilationMapper.mapCompilation2CompilationDto(addedCompilation);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationDto compDto, Long compId) {
        final List<Event> events = eventRepository.findAllById(compDto.getEvents());
        final Compilation compilation = findCompilationById(compId);

        if (compDto.getTitle() == null) {
            compDto.setTitle(compilation.getTitle());
        }
        if(compDto.getPinned() == null) {
            compDto.setPinned(compilation.getPinned());
        }

        final Compilation updateComp = CompilationMapper.mapUpdateCompilationDto2Compilation(compDto, events);
        updateComp.setId(compId);

        final Compilation updatedComp = compRepository.save(updateComp);

        return CompilationMapper.mapCompilation2CompilationDto(updatedComp);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        final Compilation compilation = findCompilationById(compId);
        return CompilationMapper.mapCompilation2CompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        final List<Compilation> compilations = compRepository.getCompilations(pinned, from, size);

        return compilations
                .stream()
                .map(CompilationMapper::mapCompilation2CompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCompilationById(Long compId) {
        findCompilationById(compId);
        compRepository.deleteById(compId);
    }

    private Compilation findCompilationById(Long compId) {
        return compRepository.findById(compId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
    }
}
