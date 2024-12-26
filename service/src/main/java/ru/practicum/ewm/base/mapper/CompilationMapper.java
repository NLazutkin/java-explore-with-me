package ru.practicum.ewm.base.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.base.dto.compilation.CompilationDto;
import ru.practicum.ewm.base.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.base.models.Compilation;
import ru.practicum.ewm.base.models.Event;

import java.util.List;
import java.util.Set;

@UtilityClass
public class CompilationMapper {

    public static Compilation mapToEntity(NewCompilationDto request, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        if (request.getPinned() == null) {
            compilation.setPinned(Boolean.FALSE);
        } else {
            compilation.setPinned(request.getPinned());
        }

        compilation.setTitle(request.getTitle());

        return compilation;
    }

    public static CompilationDto mapToDto(Compilation entity) {
        CompilationDto dto = new CompilationDto();
        dto.setId(entity.getId());
        dto.setEvents(EventMapper.mapToListShortDto(entity.getEvents()));
        dto.setPinned(entity.getPinned());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public static Compilation updateFields(Compilation compilation, UpdateCompilationRequest request, Set<Event> events) {
        if (request.hasEvents()) {
            compilation.setEvents(events);
        }

        if (request.hasPinned()) {
            compilation.setPinned(request.getPinned());
        }

        if (request.hasTitle()) {
            compilation.setTitle(request.getTitle());
        }
        return compilation;
    }

    public static List<CompilationDto> mapToListDto(List<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::mapToDto).toList();
    }
}
