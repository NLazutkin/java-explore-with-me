package ru.practicum.ewm.personal.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.base.dto.event.*;
import ru.practicum.ewm.personal.service.event.PersonalUserEventService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{user-id}/events")
public class PersonalUsersEventController {
    private static final String PV_USER_ID = "user-id";
    private static final String M_EVENT_ID = "/{event-id}";
    private static final String PV_EVENT_ID = "event-id";
    private static final String M_REQUESTS_ID = "/requests";

    private final PersonalUserEventService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable(PV_USER_ID) Long userId,
                             @RequestBody @Valid NewEventDto request) {
        log.info("Получен запрос POST /users/{}/events c событием {}", userId, request);
        return service.save(userId, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> getUserEvents(@PathVariable(PV_USER_ID) Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /users/{}/events с параметрами from = {}, size = {}", userId, from, size);
        return service.getAll(userId, from, size);
    }

    @GetMapping(M_EVENT_ID)
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getUserEvent(@PathVariable(PV_USER_ID) Long userId,
                                     @PathVariable(PV_EVENT_ID) Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}", userId, eventId);
        return service.get(userId, eventId);
    }

    @GetMapping(M_EVENT_ID + M_REQUESTS_ID)
    @ResponseStatus(HttpStatus.OK)
    public Collection<ParticipationRequestDto> getUserEventRequests(@PathVariable(PV_USER_ID) Long userId,
                                                                    @PathVariable(PV_EVENT_ID) Long eventId) {
        log.info("Получен запрос GET /users/{}/events/{}/requests", userId, eventId);
        return service.getRequests(userId, eventId);
    }

    @PatchMapping(M_EVENT_ID)
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable(PV_USER_ID) Long userId,
                               @PathVariable(PV_EVENT_ID) Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest request) {
        log.info("Получен запрос PATCH /users/{}/events/{} на обновление события", userId, eventId);
        return service.update(userId, eventId, request);
    }

    @PatchMapping(M_EVENT_ID + M_REQUESTS_ID)
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable(PV_USER_ID) Long userId,
                                                               @PathVariable(PV_EVENT_ID) Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Получен запрос PATCH /users/{}/events/{} на обновление заявок в статус {}", userId, eventId, request.getStatus());
        return service.updateRequestsStatus(userId, eventId, request);
    }
}
