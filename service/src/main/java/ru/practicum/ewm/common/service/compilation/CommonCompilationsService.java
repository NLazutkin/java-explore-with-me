package ru.practicum.ewm.common.service.compilation;

import ru.practicum.ewm.base.dto.compilation.CompilationDto;

import java.util.Collection;

public interface CommonCompilationsService {
    Collection<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto get(Long compId);
}
