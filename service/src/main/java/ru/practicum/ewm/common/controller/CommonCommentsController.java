package ru.practicum.ewm.common.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.comment.CommentFullDto;
import ru.practicum.ewm.base.dto.comment.CommentShortDto;
import ru.practicum.ewm.common.service.common.CommonCommentService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommonCommentsController {
    private final CommonCommentService service;

    @GetMapping("/event/{event-id}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentShortDto> getAll(@PathVariable("event-id") Long eventId,
                                              @RequestParam(name = "positive", required = false) Boolean positive,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /comments/event/{} с параметрами from = {}, size = {}", eventId, from, size);
        return service.getAll(eventId, positive, from, size);
    }

    @GetMapping("/{com-id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto get(@PathVariable("com-id") Long comId) {
        log.info("Получен запрос GET /comments/{}", comId);
        return service.get(comId);
    }
}
