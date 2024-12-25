package ru.practicum.ewm.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin.service.compilation.AdminCompilationService;
import ru.practicum.ewm.base.dto.compilation.CompilationDto;
import ru.practicum.ewm.base.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.base.dto.compilation.UpdateCompilationRequest;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {
    private static final String M_COMP_ID = "/{comp-id}";
    private static final String PV_COMP_ID = "comp-id";

    private final AdminCompilationService service;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto save(@RequestBody @Valid NewCompilationDto request) {
        log.info("Получен запрос POST /admin/compilations c новой подборкой: \"{}\"", request.getTitle());
        return service.save(request);
    }

    @PatchMapping(M_COMP_ID)
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable(PV_COMP_ID) Long eventId,
                                 @RequestBody @Valid UpdateCompilationRequest request) {
        log.info("Получен запрос PATCH /admin/compilations/{} на изменение подборки", eventId);
        return service.update(request, eventId);
    }

    @DeleteMapping(M_COMP_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(PV_COMP_ID) Long compId) {
        log.info("Получен запрос DELETE /admin/compilations/{}", compId);
        service.delete(compId);
    }
}
