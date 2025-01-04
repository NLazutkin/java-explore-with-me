package ru.practicum.ewm.admin.service.comment;

import ru.practicum.ewm.admin.dto.NewParamCommentDto;
import ru.practicum.ewm.base.dto.comment.CommentFullDto;
import ru.practicum.ewm.base.dto.comment.UpdateCommentRequest;

import java.util.Collection;

public interface AdminCommentService {
    Collection<CommentFullDto> getComments(NewParamCommentDto params);

    CommentFullDto update(UpdateCommentRequest request, Long comId);

    void delete(Long comId);
}
