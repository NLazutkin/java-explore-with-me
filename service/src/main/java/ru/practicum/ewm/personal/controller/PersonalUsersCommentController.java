package ru.practicum.ewm.personal.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.comment.CommentFullDto;
import ru.practicum.ewm.base.dto.comment.NewCommentDto;
import ru.practicum.ewm.base.dto.comment.UpdateCommentRequest;
import ru.practicum.ewm.personal.service.comment.PersonalUserCommentService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{user-id}/comments")
public class PersonalUsersCommentController {
    private static final String PV_USER_ID = "user-id";
    private static final String M_EVENT_ID = "/{event-id}";
    private static final String PV_EVENT_ID = "event-id";
    private static final String M_COM_ID = "/{com-id}";
    private static final String PV_COM_ID = "com-id";

    private final PersonalUserCommentService service;

    @PostMapping(M_EVENT_ID)
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto save(@PathVariable(PV_USER_ID) Long userId,
                               @PathVariable(PV_EVENT_ID) Long eventId,
                               @RequestBody @Valid NewCommentDto request) {
        log.info("Получен запрос POST /users/{}/events c событием {}", userId, request);
        return service.save(userId, eventId, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentFullDto> getUserComments(@PathVariable(PV_USER_ID) Long userId,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                      @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /users/{}/comments с параметрами from = {}, size = {}", userId, from, size);
        return service.getAll(userId, from, size);
    }

    @GetMapping(M_EVENT_ID)
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentFullDto> getUserEventComments(@PathVariable(PV_USER_ID) Long userId,
                                                           @PathVariable(PV_EVENT_ID) Long eventId,
                                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /users/{}/comments/{} с параметрами from = {}, size = {}", userId, eventId, from, size);
        return service.getAllByEvent(userId, eventId, from, size);
    }

    @GetMapping(M_EVENT_ID + M_COM_ID)
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto getUserEventComments(@PathVariable(PV_USER_ID) Long userId,
                                               @PathVariable(PV_EVENT_ID) Long eventId,
                                               @PathVariable(PV_COM_ID) Long comId) {
        log.info("Получен запрос GET /users/{}/comments/{}/{}", userId, eventId, comId);
        return service.getCommentByEvent(userId, eventId, comId);
    }

    @PatchMapping(M_COM_ID)
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto update(@PathVariable(PV_USER_ID) Long userId,
                                 @PathVariable(PV_COM_ID) Long comId,
                                 @RequestBody @Valid UpdateCommentRequest request) {
        log.info("Получен запрос PATCH /users/{}/comments/{} на обновление комментария", userId, comId);
        return service.update(userId, comId, request);
    }

    @DeleteMapping(M_COM_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(PV_USER_ID) Long userId,
                       @PathVariable(PV_COM_ID) Long comId) {
        log.info("Получен запрос DELETE /users/{}/comments/{}", userId, comId);
        service.delete(userId, comId);
    }
}
