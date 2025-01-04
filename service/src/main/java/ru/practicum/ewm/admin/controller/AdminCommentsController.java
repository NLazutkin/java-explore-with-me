package ru.practicum.ewm.admin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin.dto.NewParamCommentDto;
import ru.practicum.ewm.admin.service.comment.AdminCommentService;
import ru.practicum.ewm.base.dto.comment.CommentFullDto;
import ru.practicum.ewm.base.dto.comment.UpdateCommentRequest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/comments")
public class AdminCommentsController {
    private static final String M_COM_ID = "/{com-id}";
    private static final String PV_COM_ID = "com-id";

    private final AdminCommentService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CommentFullDto> getComments(@RequestParam(required = false, defaultValue = "") String text,
                                                  @RequestParam(required = false, defaultValue = "") List<Long> users,
                                                  @RequestParam(required = false, defaultValue = "") List<Long> events,
                                                  @RequestParam(required = false) Boolean positive,
                                                  @RequestParam(required = false) LocalDateTime rangeStart,
                                                  @RequestParam(required = false) LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        NewParamCommentDto params = new NewParamCommentDto(text, users, events, positive, rangeStart, rangeEnd, from, size);
        log.info("Получен запрос GET /admin/comments со списком параметров {}", params);
        return service.getComments(params);
    }

    @PatchMapping(M_COM_ID)
    @ResponseStatus(HttpStatus.OK)
    public CommentFullDto update(@PathVariable(PV_COM_ID) Long comId,
                                 @RequestBody @Valid UpdateCommentRequest request) {
        log.info("Получен запрос PATCH /admin/comments/{}", comId);
        return service.update(request, comId);
    }

    @DeleteMapping(M_COM_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(PV_COM_ID) Long comId) {
        log.info("Получен запрос DELETE /admin/comments/{}", comId);
        service.delete(comId);
    }
}
