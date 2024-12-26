package ru.practicum.ewm.admin.service.compilation;

import ru.practicum.ewm.base.dto.compilation.CompilationDto;
import ru.practicum.ewm.base.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.compilation.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto save(NewCompilationDto request);

    CompilationDto update(UpdateCompilationRequest request, Long compId);

    void delete(Long compId);
}
