package ru.practicum.ewm.personal.service.comment;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.comment.CommentFullDto;
import ru.practicum.ewm.base.dto.comment.NewCommentDto;
import ru.practicum.ewm.base.dto.comment.UpdateCommentRequest;
import ru.practicum.ewm.base.enums.States;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.CommentMapper;
import ru.practicum.ewm.base.models.Comment;
import ru.practicum.ewm.base.models.Event;
import ru.practicum.ewm.base.models.User;
import ru.practicum.ewm.base.repository.comment.CommentRepository;
import ru.practicum.ewm.base.repository.event.EventRepository;
import ru.practicum.ewm.base.repository.user.UserRepository;

import java.util.Collection;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PersonalUserCommentServiceImpl implements PersonalUserCommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    EventRepository eventRepository;

    @Autowired
    public PersonalUserCommentServiceImpl(CommentRepository commentRepository,
                                          UserRepository userRepository,
                                          EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    private Comment findById(Long comId) {
        return commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException(String.format("Комментарий c ID %d не найден", comId)));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с ID %dне найдено", eventId)));
    }

    @Override
    public CommentFullDto save(Long userId, Long eventId, NewCommentDto request) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (!event.getState().equals(States.PUBLISHED)) {
            throw new ConflictException("Комментарий может быть добавлен только к опубликованному событию!");
        }

        Comment comment = CommentMapper.mapToEntity(request, user, event);

        try {
            comment = commentRepository.save(comment);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Сохраняем данные о комментарии от пользователя {} к событию {}", user.getName(), event.getTitle());
        return CommentMapper.mapToDto(comment);
    }

    @Override
    public Collection<CommentFullDto> getAll(Long userId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));

        log.info("Получаем данные о всех комментариях пользователя c ID {}", userId);
        return CommentMapper.mapToListDto(commentRepository.findAllByAuthorId(userId, pageRequest));
    }

    @Override
    public Collection<CommentFullDto> getAllByEvent(Long userId, Long eventId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        Event event = findEventById(eventId);

        log.info("Получаем данные о всех комментариях пользователя c ID {} для события {}", userId, event.getTitle());
        return CommentMapper.mapToListDto(commentRepository.findAllByAuthorIdAndEventId(userId, eventId, pageRequest));
    }

    @Override
    public CommentFullDto getCommentByEvent(Long userId, Long eventId, Long comId) {
        Event event = findEventById(eventId);

        log.info("Получаем данные о комментарии с ID {} пользователя c ID {} для события {}", comId, userId, event.getTitle());
        return CommentMapper.mapToDto(commentRepository.findByIdAndAuthorIdAndEventId(comId, userId, eventId));
    }

    @Override
    public CommentFullDto update(Long userId, Long comId, UpdateCommentRequest request) {
        User user = findUserById(userId);
        Comment comment = findById(comId);

        if (!comment.getAuthor().equals(user)) {
            throw new ConflictException("Комментарий может отредактировать только его автор!");
        }

        Comment updatedComment = CommentMapper.updateFields(comment, request);

        try {
            updatedComment = commentRepository.save(updatedComment);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Пользователь с ID {} редактирует комментарий с ID {}", userId, comId);
        return CommentMapper.mapToDto(updatedComment);
    }

    @Override
    public void delete(Long userId, Long comId) {
        User user = findUserById(userId);
        Comment comment = findById(comId);

        if (!comment.getAuthor().equals(user)) {
            throw new ConflictException("Комментарий может удалить только его автор!");
        }

        log.info("Пользователь с ID {} удаляет комментарий с ID {}", userId, comId);
        commentRepository.delete(comment);
    }
}
