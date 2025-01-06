package ru.practicum.ewm.common.service.common;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.comment.CommentFullDto;
import ru.practicum.ewm.base.dto.comment.CommentShortDto;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.CommentMapper;
import ru.practicum.ewm.base.models.Comment;
import ru.practicum.ewm.base.repository.comment.CommentRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommonCommentServiceImpl implements CommonCommentService {
    CommentRepository commentRepository;

    @Autowired
    public CommonCommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    private Comment findById(Long comId) {
        return commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Комментарий c ID %d не найден", comId)));
    }

    @Override
    public Collection<CommentShortDto> getAll(Long eventId, Boolean positive, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "created"));

        List<Comment> comments;
        if (positive != null) {
            comments = commentRepository.findAllByEventIdAndPositive(eventId, positive, pageRequest);
        } else {
            comments = commentRepository.findAllByEventId(eventId, pageRequest);
        }

        log.info("Получаем {} коммент. о событии для посетителей сервиса", comments.size());
        return CommentMapper.mapToListShortDto(comments);
    }

    @Override
    public CommentFullDto get(Long comId) {
        return CommentMapper.mapToDto(findById(comId));
    }
}
