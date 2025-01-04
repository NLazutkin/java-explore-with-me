package ru.practicum.ewm.personal.service.request;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.base.enums.States;
import ru.practicum.ewm.base.enums.Statuses;
import ru.practicum.ewm.base.exceptions.BadRequestException;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.RequestMapper;
import ru.practicum.ewm.base.models.Event;
import ru.practicum.ewm.base.models.Request;
import ru.practicum.ewm.base.models.User;
import ru.practicum.ewm.base.repository.event.EventRepository;
import ru.practicum.ewm.base.repository.request.RequestRepository;
import ru.practicum.ewm.base.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PersonalUserRequestServiceImpl implements PersonalUserRequestService {
    UserRepository userRepository;
    EventRepository eventRepository;
    RequestRepository requestRepository;

    @Autowired
    public PersonalUserRequestServiceImpl(UserRepository userRepository,
                                          EventRepository eventRepository,
                                          RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.requestRepository = requestRepository;
    }

    private Request findByIdAndRequesterId(Long requestId, Long userId) {
        return requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос c ID %d пользователя с ID %d не найден",
                        requestId, userId)));
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
    @Transactional
    public ParticipationRequestDto save(Long userId, Long eventId) {
        if (userId == null || eventId == null) {
            throw new BadRequestException("Не задан параметр ID пользователя или события");
        }

        Event event = findEventById(eventId);
        User user = findUserById(userId);

        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException(String.format("Запрос на событие с ID %d от пользователя с ID %d уже существует",
                    eventId, userId));
        }

        if (event.getInitiator().equals(user)) {
            throw new ConflictException("Нельзя создать запрос на участие в своем событии");
        }

        if (!event.getState().equals(States.PUBLISHED)) {
            throw new ConflictException("Нельзя участвовать в неопубликованном событии");
        }

        if (!event.getParticipantLimit().equals(0L) && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException("Достигнут лимит запросов на участие в событии");
        }

        if (event.getRequestModeration().equals(Boolean.FALSE)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        Request request = RequestMapper.mapToEntity(event, user);
        try {
            request = requestRepository.save(request);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Сохраняем данные о запросе с ID {} к событию {} созданном пользователем {}",
                request.getId(), event.getTitle(), user.getName());
        return RequestMapper.mapToDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ParticipationRequestDto> get(Long userId) {
        User user = findUserById(userId);

        List<Request> requests = requestRepository.findAllByRequesterId(userId);

        log.info("Получаем данные о всех {} запросах пользователях c ID {}", requests.size(), user.getName());
        return RequestMapper.mapToListDto(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto update(Long userId, Long requestId) {
        Request request = findByIdAndRequesterId(requestId, userId);

        request.setStatus(Statuses.CANCELED);

        try {
            request = requestRepository.save(request);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Пользователь с ID {} отменяет запрос с ID \"{}\"", userId, request.getId());
        return RequestMapper.mapToDto(request);
    }
}
