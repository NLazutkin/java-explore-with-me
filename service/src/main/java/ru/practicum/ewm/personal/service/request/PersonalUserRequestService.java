package ru.practicum.ewm.personal.service.request;

import ru.practicum.ewm.base.dto.request.ParticipationRequestDto;

import java.util.Collection;

public interface PersonalUserRequestService {
    ParticipationRequestDto save(Long userId, Long eventId);

    Collection<ParticipationRequestDto> get(Long userId);

    ParticipationRequestDto update(Long userId, Long requestId);
}
