package ru.practicum.ewm.personal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.ParticipationRequestDto;
import ru.practicum.ewm.personal.service.request.PersonalUserRequestService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{user-id}/requests")
public class PersonalUsersRequestController {
    private static final String PV_USER_ID = "user-id";
    private static final String M_REQUEST_CANCEL_ID = "/{request-id}/cancel";
    private static final String PV_REQUEST_ID = "request-id";

    private final PersonalUserRequestService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@PathVariable(PV_USER_ID) Long userId,
                                        @RequestParam(required = false) Long eventId) {
        log.info("Получен запрос POST /users/{}/requests на участие в событии с ID {}", userId, eventId);
        return service.save(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ParticipationRequestDto> get(@PathVariable(PV_USER_ID) Long userId) {
        log.info("Получен запрос GET /users/{}/requests", userId);
        return service.get(userId);
    }

    @PatchMapping(M_REQUEST_CANCEL_ID)
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto update(@PathVariable(PV_USER_ID) Long userId,
                                          @PathVariable(PV_REQUEST_ID) Long requestId) {
        log.info("Получен запрос PATCH /users/{}/requests/{}/cancel на отмену заявки", userId, requestId);
        return service.update(userId, requestId);
    }
}
