package ru.practicum.ewm.base.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.base.dto.event.*;
import ru.practicum.ewm.base.dto.location.LocationDto;
import ru.practicum.ewm.base.enums.AdminStateAction;
import ru.practicum.ewm.base.enums.States;
import ru.practicum.ewm.base.enums.UserStateAction;
import ru.practicum.ewm.base.models.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public static Event mapToEntity(NewEventDto dto, Category category, User initiator) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(category);
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setInitiator(initiator);
        event.setLocation(new Location(dto.getLocation().getLat(), dto.getLocation().getLon()));

        if (dto.hasPaid()) {
            event.setPaid(dto.getPaid());
        } else {
            event.setPaid(Boolean.FALSE);
        }

        if (dto.hasParticipantLimit()) {
            event.setParticipantLimit(dto.getParticipantLimit());
        } else {
            event.setParticipantLimit(0L);
        }

        if (dto.hasRequestModeration()) {
            event.setRequestModeration(dto.getRequestModeration());
        } else {
            event.setRequestModeration(Boolean.TRUE);
        }

        event.setPublishedOn(LocalDateTime.now());
        event.setState(States.PENDING);
        event.setTitle(dto.getTitle());
        event.setViews(0L);

        return event;
    }

    public static Event updateAdminFields(Event event, UpdateEventAdminRequest request, Category category) {
        if (request.hasAnnotation()) {
            event.setAnnotation(request.getAnnotation());
        }

        if (category != null) {
            event.setCategory(category);
        }

        if (request.hasDescription()) {
            event.setDescription(request.getDescription());
        }

        if (request.hasEventDate()) {
            event.setEventDate(request.getEventDate());
        }

        if (request.hasLocation()) {
            event.setLocation(new Location(request.getLocation().getLat(), request.getLocation().getLon()));
        }

        if (request.hasPaid()) {
            event.setPaid(request.getPaid());
        }

        if (request.hasParticipantLimit()) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.hasRequestModeration()) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.hasTitle()) {
            event.setTitle(request.getTitle());
        }

        if (request.hasStateAction() && request.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)) {
            event.setState(States.PUBLISHED);
        }

        if (request.hasStateAction() && request.getStateAction().equals(AdminStateAction.REJECT_EVENT)) {
            event.setState(States.CANCELED);
        }

        return event;
    }

    public static Event updateUserFields(Event event, UpdateEventUserRequest request, Category category) {
        if (request.hasAnnotation()) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.hasCategory()) {
            event.setCategory(category);
        }

        if (request.hasDescription()) {
            event.setDescription(request.getDescription());
        }

        if (request.hasEventDate()) {
            event.setEventDate(request.getEventDate());
        }

        if (request.hasLocation()) {
            event.setLocation(new Location(request.getLocation().getLat(), request.getLocation().getLon()));
        }

        if (request.hasPaid()) {
            event.setPaid(request.getPaid());
        }

        if (request.hasParticipantLimit()) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.hasRequestModeration()) {
            event.setRequestModeration(request.getRequestModeration());
        }

        if (request.hasTitle()) {
            event.setTitle(request.getTitle());
        }

        if (request.hasStateAction() && request.getStateAction().equals(UserStateAction.SEND_TO_REVIEW)) {
            event.setState(States.PENDING);
        }

        if (request.hasStateAction() && request.getStateAction().equals(UserStateAction.CANCEL_REVIEW)) {
            event.setState(States.CANCELED);
        }

        return event;
    }

    public static EventFullDto mapToDto(Event entity) {
        EventFullDto dto = new EventFullDto();
        dto.setId(entity.getId());
        dto.setAnnotation(entity.getAnnotation());
        dto.setCategory(CategoryMapper.mapToDto(entity.getCategory()));
        dto.setConfirmedRequests(entity.getConfirmedRequests());
        dto.setCreatedOn(entity.getCreatedOn());
        dto.setDescription(entity.getDescription());
        dto.setEventDate(entity.getEventDate());
        dto.setInitiator(UserMapper.mapToShortDto(entity.getInitiator()));
        dto.setLocation(new LocationDto(entity.getLocation().getLat(), entity.getLocation().getLon()));
        dto.setPaid(entity.getPaid());
        dto.setParticipantLimit(entity.getParticipantLimit());
        dto.setPublishedOn(entity.getPublishedOn());
        dto.setRequestModeration(entity.getRequestModeration());
        dto.setState(entity.getState());
        dto.setTitle(entity.getTitle());
        dto.setViews(entity.getViews());

        return dto;
    }

    public static EventFullDto mapToDtoWithStat(Event entity, Long views) {
        EventFullDto dto = mapToDto(entity);
        dto.setViews(views);

        return dto;
    }

    public static EventAdvancedDto mapToAdvancedDto(Event entity, Long views, List<Comment> comments) {
        EventAdvancedDto dto = new EventAdvancedDto();
        dto.setId(entity.getId());
        dto.setAnnotation(entity.getAnnotation());
        dto.setCategory(CategoryMapper.mapToDto(entity.getCategory()));
        dto.setConfirmedRequests(entity.getConfirmedRequests());
        dto.setCreatedOn(entity.getCreatedOn());
        dto.setDescription(entity.getDescription());
        dto.setEventDate(entity.getEventDate());
        dto.setInitiator(UserMapper.mapToShortDto(entity.getInitiator()));
        dto.setLocation(new LocationDto(entity.getLocation().getLat(), entity.getLocation().getLon()));
        dto.setPaid(entity.getPaid());
        dto.setParticipantLimit(entity.getParticipantLimit());
        dto.setPublishedOn(entity.getPublishedOn());
        dto.setRequestModeration(entity.getRequestModeration());
        dto.setState(entity.getState());
        dto.setTitle(entity.getTitle());
        dto.setViews(entity.getViews());
        dto.setViews(views);
        if (comments != null) {
            dto.setComments(CommentMapper.mapToListShortDto(comments));
        }

        return dto;
    }

    public static EventShortDto mapToShortDto(Event entity) {
        EventShortDto dto = new EventShortDto();
        dto.setId(entity.getId());
        dto.setAnnotation(entity.getAnnotation());
        dto.setCategory(CategoryMapper.mapToDto(entity.getCategory()));
        dto.setConfirmedRequests(entity.getConfirmedRequests());
        dto.setEventDate(entity.getEventDate());
        dto.setInitiator(UserMapper.mapToShortDto(entity.getInitiator()));
        dto.setPaid(entity.getPaid());
        dto.setTitle(entity.getTitle());
        dto.setViews(entity.getViews());

        return dto;
    }

    public static EventShortDto mapToShortDtoWithStat(Event entity, Long views) {
        EventShortDto dto = mapToShortDto(entity);
        dto.setViews(views);

        return dto;
    }

    public static List<EventFullDto> mapToListDto(List<Event> listEntity) {
        return listEntity.stream().map(EventMapper::mapToDto).collect(Collectors.toList());
    }

    public static Set<EventShortDto> mapToListShortDto(Set<Event> setEntity) {
        return setEntity.stream().map(EventMapper::mapToShortDto).collect(Collectors.toSet());
    }

    public static Set<EventShortDto> mapToListShortDto(Set<Event> setEntity, Map<Long, Long> statsMap) {
        return setEntity.stream()
                .map(elem -> mapToShortDtoWithStat(elem, statsMap.get(elem.getId())))
                .collect(Collectors.toSet());
    }

    public static Set<EventAdvancedDto> mapToListAdvancedDto(Set<Event> setEntity,
                                                             Map<Long, Long> statsMap,
                                                             Map<Event, List<Comment>> commentsMap) {
        return setEntity.stream()
                .map(elem -> mapToAdvancedDto(elem,
                        statsMap.get(elem.getId()),
                        commentsMap.getOrDefault(elem, Collections.emptyList())))
                .collect(Collectors.toSet());
    }
}
