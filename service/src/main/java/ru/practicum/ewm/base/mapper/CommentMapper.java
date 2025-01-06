package ru.practicum.ewm.base.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.base.dto.comment.CommentFullDto;
import ru.practicum.ewm.base.dto.comment.CommentShortDto;
import ru.practicum.ewm.base.dto.comment.NewCommentDto;
import ru.practicum.ewm.base.dto.comment.UpdateCommentRequest;
import ru.practicum.ewm.base.models.Comment;
import ru.practicum.ewm.base.models.Event;
import ru.practicum.ewm.base.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public static Comment mapToEntity(NewCommentDto request, User author, Event event) {
        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        comment.setLastModify(LocalDateTime.now());
        comment.setPositive(request.getPositive());

        return comment;
    }

    public static CommentFullDto mapToDto(Comment comment) {
        CommentFullDto dto = new CommentFullDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setEvent(comment.getEvent().getId());
        dto.setAuthor(UserMapper.mapToDto(comment.getAuthor()));
        dto.setCreated(comment.getCreated());
        dto.setPositive(comment.getPositive());
        dto.setLastModify(comment.getCreated());

        return dto;
    }

    public static CommentShortDto mapToShortDto(Comment comment) {
        CommentShortDto dto = new CommentShortDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthor(UserMapper.mapToShortDto(comment.getAuthor()));
        dto.setPositive(comment.getPositive());
        dto.setLastModify(comment.getCreated());

        return dto;
    }

    public static Comment updateFields(Comment entity, UpdateCommentRequest request) {
        if (request.hasText()) {
            entity.setText(request.getText());
        }

        if (request.hasPositive()) {
            entity.setPositive(request.getPositive());
        }

        entity.setLastModify(LocalDateTime.now());

        return entity;
    }

    public static List<CommentFullDto> mapToListDto(List<Comment> listEntity) {
        return listEntity.stream().map(CommentMapper::mapToDto).collect(Collectors.toList());
    }

    public static List<CommentShortDto> mapToListShortDto(List<Comment> listEntity) {
        return listEntity.stream().map(CommentMapper::mapToShortDto).collect(Collectors.toList());
    }
}
