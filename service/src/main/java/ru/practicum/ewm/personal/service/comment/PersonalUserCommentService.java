package ru.practicum.ewm.personal.service.comment;

import ru.practicum.ewm.base.dto.comment.CommentFullDto;
import ru.practicum.ewm.base.dto.comment.NewCommentDto;
import ru.practicum.ewm.base.dto.comment.UpdateCommentRequest;

import java.util.Collection;

public interface PersonalUserCommentService {

    CommentFullDto save(Long userId, Long eventId, NewCommentDto request);

    Collection<CommentFullDto> getAll(Long userId, Integer from, Integer size);

    Collection<CommentFullDto> getAllByEvent(Long userId, Long eventId, Integer from, Integer size);

    CommentFullDto getCommentByEvent(Long userId, Long eventId, Long comId);

    CommentFullDto update(Long userId, Long comId, UpdateCommentRequest request);

    void delete(Long userId, Long comId);
}
