package ru.practicum.ewm.common.service.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.dto.event.EventAdvancedDto;
import ru.practicum.ewm.base.enums.States;
import ru.practicum.ewm.base.exceptions.BadRequestException;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.EventMapper;
import ru.practicum.ewm.base.models.Comment;
import ru.practicum.ewm.base.models.Event;
import ru.practicum.ewm.base.models.EventCriteria;
import ru.practicum.ewm.base.repository.comment.CommentRepository;
import ru.practicum.ewm.base.repository.event.EventRepository;
import ru.practicum.ewm.base.util.Statistic.Statistic;
import ru.practicum.ewm.common.dto.EventRequestParam;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommonEventsServiceImpl implements CommonEventsService {

    EventRepository eventRepository;

    CommentRepository commentRepository;

    Statistic statistic;

    @Autowired
    public CommonEventsServiceImpl(EventRepository eventRepository, CommentRepository commentRepository, Statistic statistic) {
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
        this.statistic = statistic;
    }

    private PageRequest getPageable(String sort, Integer from, Integer size) {
        PageRequest pageRequest = null;
        if (sort == null || sort.equalsIgnoreCase("EVENT_DATE")) {
            pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "event_date"));
        } else if (sort.equalsIgnoreCase("VIEWS")) {
            pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "views"));
        }

        return pageRequest;
    }

    private Event findById(Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty() || !event.get().getState().equals(States.PUBLISHED)) {
            throw new NotFoundException(String.format("Событие c ID %d не доступно", id));
        }

        return event.get();
    }

    @Override
    @Transactional
    public Collection<EventAdvancedDto> getAll(EventRequestParam params) {
        if (params.expectedBaseCriteria()) {
            throw new BadRequestException(String.format("Не заданы базовые параметры поиска. text = %s " +
                    "и единственное значение category = %s", params.getText(), params.getCategories().getFirst()));
        }

        PageRequest pageRequest = getPageable(params.getSort(), params.getFrom(), params.getSize());

        EventCriteria eventCriteria = new EventCriteria(params.getText(),
                params.getCategories(),
                params.getPaid(),
                params.getRangeStart(),
                params.getRangeEnd(),
                params.getOnlyAvailable());

        Set<Event> events = eventRepository.findAllWithCriteria(pageRequest, eventCriteria).toSet();

        Map<Long, Long> statsMap = statistic.getStatistic(events, Boolean.FALSE);

        Map<Event, List<Comment>> eventsWithComments = commentRepository
                .findAllByEventIdIn(events.stream().map(Event::getId).toList())
                .stream()
                .collect(groupingBy(Comment::getEvent, toList()));

        statistic.saveStatistic(params.getRequest());
        log.info("Получаем публичный список из {} событий с добавлением в статистику", events.size());
        return EventMapper.mapToListAdvancedDto(events, statsMap, eventsWithComments);
    }

    @Override
    @Transactional
    public EventAdvancedDto get(Long id, HttpServletRequest request) {
        Event event = findById(id);

        List<Comment> comments = commentRepository.findAllByEventId(id);

        Map<Long, Long> statsMap = statistic.getStatistic(
                event.getPublishedOn(),
                LocalDateTime.now(),
                List.of(request.getRequestURI()),
                Boolean.TRUE
        );

        statistic.saveStatistic(request);
        log.info("Получаем полную информацию о событии {} с добавлением в статистику", event.getTitle());
        return EventMapper.mapToAdvancedDto(event, statsMap.getOrDefault(id, 0L), comments);
    }
}
