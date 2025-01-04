package ru.practicum.ewm.admin.service.event;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.admin.dto.NewParamEventDto;
import ru.practicum.ewm.base.dto.event.EventFullDto;
import ru.practicum.ewm.base.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.base.enums.States;
import ru.practicum.ewm.base.exceptions.BadRequestException;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.EventMapper;
import ru.practicum.ewm.base.models.Category;
import ru.practicum.ewm.base.models.Event;
import ru.practicum.ewm.base.repository.category.CategoryRepository;
import ru.practicum.ewm.base.repository.event.EventRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminEventServiceImpl implements AdminEventService {

    EventRepository eventRepository;
    CategoryRepository categoryRepository;

    @Autowired
    public AdminEventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    private Event findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Событие c ID %d не найдено", eventId)));
    }

    private Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория c ID %d не найдена", catId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<EventFullDto> getEvents(NewParamEventDto params) {
        PageRequest pageRequest = PageRequest.of(params.getFrom() / params.getSize(),
                params.getSize(),
                Sort.by(Sort.Direction.ASC, "id"));

        List<Event> events = eventRepository.findAllWithCriteria(pageRequest, params).toList();

        log.info("Получаем данные о {} событиях", events.size());
        return EventMapper.mapToListDto(events);
    }

    @Override
    @Transactional
    public EventFullDto update(UpdateEventAdminRequest request, Long eventId) {

        if (request.hasEventDate() && request.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Поле: eventDate. Ошибка: дата и время, на которые запланировано мероприятие, " +
                    "не могут быть ранее, чем через час после текущего момента. Значение: " + request.getEventDate());
        }

        Event findEvent = findById(eventId);

        if (findEvent.getState().equals(States.PUBLISHED)) {
            throw new ConflictException("Невозможно обновить т.к. событие уже опубликовано");
        } else if (findEvent.getState().equals(States.CANCELED)) {
            throw new ConflictException("Невозможно обновить т.к. событие уже отменено");
        }

        Category category = null;
        if (request.hasCategory() && !findEvent.getCategory().getId().equals(request.getCategory())) {
            category = findCategoryById(request.getCategory());
        }

        Event updatedEvent = EventMapper.updateAdminFields(findEvent, request, category);

        try {
            updatedEvent = eventRepository.save(updatedEvent);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Администратор обновляет событие \"{}\"", updatedEvent.getTitle());
        return EventMapper.mapToDto(updatedEvent);
    }
}
