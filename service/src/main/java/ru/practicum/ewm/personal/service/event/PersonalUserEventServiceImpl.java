package ru.practicum.ewm.personal.service.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.base.dto.event.*;
import ru.practicum.ewm.base.enums.States;
import ru.practicum.ewm.base.enums.Statuses;
import ru.practicum.ewm.base.exceptions.BadRequestException;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.EventMapper;
import ru.practicum.ewm.base.mapper.RequestMapper;
import ru.practicum.ewm.base.models.Category;
import ru.practicum.ewm.base.models.Event;
import ru.practicum.ewm.base.models.Request;
import ru.practicum.ewm.base.models.User;
import ru.practicum.ewm.base.repository.category.CategoryRepository;
import ru.practicum.ewm.base.repository.event.EventRepository;
import ru.practicum.ewm.base.repository.request.RequestRepository;
import ru.practicum.ewm.base.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PersonalUserEventServiceImpl implements PersonalUserEventService {
    UserRepository userRepository;
    EventRepository eventRepository;
    CategoryRepository categoryRepository;
    RequestRepository requestRepository;

    @Autowired
    public PersonalUserEventServiceImpl(UserRepository userRepository,
                                        EventRepository eventRepository,
                                        CategoryRepository categoryRepository,
                                        RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    private Event findByIdAndInitiatorId(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие с ID %d, пользователя c ID %d не найдено", eventId, userId)));
    }

    private Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория c ID %d не найдена", catId)));
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Поле: eventDate. Ошибка: дата и время, на которые запланировано мероприятие, " +
                    "не могут быть ранее, чем через 2 часа после текущего момента. Значение: " + eventDate);
        }
    }

    @Override
    @Transactional
    public EventFullDto save(Long userId, NewEventDto request) {
        checkEventDate(request.getEventDate());

        Event event = EventMapper.mapToEntity(request, findCategoryById(request.getCategory()), findUserById(userId));
        try {
            event = eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Сохраняем данные о событии {} созданном пользователем c ID {}", request.getTitle(), userId);
        return EventMapper.mapToDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));

        Set<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest);

        log.info("Получаем данные о всех {} событиях пользователях c ID {}", events.size(), userId);
        return EventMapper.mapToListShortDto(events);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto get(Long userId, Long eventId) {
        Event events = findByIdAndInitiatorId(eventId, userId);

        log.info("Событие {}, пользователя {} найдено", events.getTitle(), events.getInitiator().getName());
        return EventMapper.mapToDto(events);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        Event event = findByIdAndInitiatorId(eventId, userId);

        log.info("Получаем данные о всех запросах на участие в событии {} пользователях c ID {}", event.getTitle(), userId);
        return RequestMapper.mapToListDto(requestRepository.findAllByEventId(eventId));
    }

    @Override
    @Transactional
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest request) {
        Event findEvent = findByIdAndInitiatorId(eventId, userId);

        checkEventDate(request.getEventDate());

        if (findEvent.getState().equals(States.PUBLISHED)) {
            throw new ConflictException("Невозможно обновить т.к. событие уже опубликовано");
        }

        Category category = null;
        if (request.hasCategory() && !findEvent.getCategory().getId().equals(request.getCategory())) {
            category = findCategoryById(request.getCategory());
        }

        Event updatedEvent = EventMapper.updateUserFields(findEvent, request, category);

        try {
            updatedEvent = eventRepository.save(updatedEvent);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Пользователь с ID {} обновляет событие \"{}\"", userId, updatedEvent.getTitle());
        return EventMapper.mapToDto(updatedEvent);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        List<ParticipationRequestDto> confirmedRequests = List.of();
        List<ParticipationRequestDto> rejectedRequests = List.of();

        List<Long> requestIds = request.getRequestIds();
        List<Request> requests = requestRepository.findAllById(requestIds);
        Event findEvent = findByIdAndInitiatorId(eventId, userId);
        String status = request.getStatus();

        // Отклонение всех заявок
        if (status.equals(Statuses.REJECTED.toString())) {
            boolean isConfirmedRequestExists = requests.stream()
                    .anyMatch(elem -> elem.getStatus().equals(Statuses.CONFIRMED));
            if (isConfirmedRequestExists) {
                throw new ConflictException("При попытке отклонить все заявки, найдена подтвержденная.");
            }
            rejectedRequests = requests.stream()
                    .peek(elem -> elem.setStatus(Statuses.REJECTED))
                    .map(RequestMapper::mapToDto)
                    .collect(Collectors.toList());

        } else if (status.equals(Statuses.CONFIRMED.toString())) {
            Long participantLimit = findEvent.getParticipantLimit();
            Long approvedRequests = findEvent.getConfirmedRequests();
            long availableParticipants = participantLimit - approvedRequests;
            long waitingParticipants = requestIds.size();

            if (participantLimit > 0 && participantLimit.equals(approvedRequests)) {
                throw new ConflictException(String.format("Событие %s достигло лимита по заявкам", findEvent.getTitle()));
            }

            if (participantLimit.equals(0L) || (waitingParticipants <= availableParticipants && !findEvent.getRequestModeration())) {
                confirmedRequests = requests.stream()
                        .peek(elem -> {
                            if (!elem.getStatus().equals(Statuses.CONFIRMED)) {
                                elem.setStatus(Statuses.CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Запрос с ID %d уже подтвержден", elem.getId()));
                            }
                        })
                        .map(RequestMapper::mapToDto)
                        .toList();

                findEvent.setConfirmedRequests(approvedRequests + waitingParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(elem -> {
                            if (!elem.getStatus().equals(Statuses.CONFIRMED)) {
                                elem.setStatus(Statuses.CONFIRMED);
                            } else {
                                throw new ConflictException(String.format("Запрос с ID %d уже подтвержден", elem.getId()));
                            }
                        })
                        .map(RequestMapper::mapToDto)
                        .toList();

                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(elem -> {
                            if (!elem.getStatus().equals(Statuses.REJECTED)) {
                                elem.setStatus(Statuses.REJECTED);
                            } else {
                                throw new ConflictException(String.format("Запрос с ID %d уже отклонен", elem.getId()));
                            }
                        })
                        .map(RequestMapper::mapToDto)
                        .toList();

                findEvent.setConfirmedRequests((long) confirmedRequests.size());
            }
        } else {
            throw new ConflictException(String.format("При попытке редактирования заявок, указан неверный статус %s", status));
        }

        try {
            eventRepository.save(findEvent);
            requestRepository.saveAll(requests);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Пользователь с ID {} обновляет статус заявок события\"{}\"", userId, findEvent.getTitle());
        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }
}
