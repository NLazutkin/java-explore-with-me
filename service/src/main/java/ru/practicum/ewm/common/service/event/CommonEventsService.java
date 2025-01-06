package ru.practicum.ewm.common.service.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.ewm.base.dto.event.EventAdvancedDto;
import ru.practicum.ewm.common.dto.EventRequestParam;

import java.util.Collection;

public interface CommonEventsService {
    Collection<EventAdvancedDto> getAll(EventRequestParam param);

    EventAdvancedDto get(Long id, HttpServletRequest request);
}
