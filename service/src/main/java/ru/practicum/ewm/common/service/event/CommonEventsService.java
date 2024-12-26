package ru.practicum.ewm.common.service.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.base.dto.event.EventFullDto;
import ru.practicum.ewm.base.dto.event.EventShortDto;
import ru.practicum.ewm.common.dto.EventRequestParam;

import java.util.Collection;

public interface CommonEventsService {
    Collection<EventShortDto> getAll(EventRequestParam param);

    EventFullDto get(Long id, HttpServletRequest request);
}
