package ru.practicum.ewm.personal.service.event;

import ru.practicum.ewm.base.dto.ParticipationRequestDto;
import ru.practicum.ewm.base.dto.event.*;

import java.util.Collection;

public interface PersonalUserEventService {
    EventFullDto save(Long userId, NewEventDto request);

    Collection<EventShortDto> getAll(Long userId, Integer from, Integer size);

    EventFullDto get(Long userId, Long eventId);

    Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest request);

    EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}
