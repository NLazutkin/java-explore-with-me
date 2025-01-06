package ru.practicum.ewm.common.service.common;

import ru.practicum.ewm.base.dto.comment.CommentFullDto;
import ru.practicum.ewm.base.dto.comment.CommentShortDto;

import java.util.Collection;

public interface CommonCommentService {
    Collection<CommentShortDto> getAll(Long eventId, Boolean positive, Integer from, Integer size);

    CommentFullDto get(Long comId);
}
