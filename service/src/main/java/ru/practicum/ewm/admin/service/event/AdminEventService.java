package ru.practicum.ewm.admin.service.event;

import ru.practicum.ewm.admin.dto.NewParamEventDto;
import ru.practicum.ewm.base.dto.event.EventFullDto;
import ru.practicum.ewm.base.dto.event.UpdateEventAdminRequest;

import java.util.Collection;

public interface AdminEventService {
    Collection<EventFullDto> getEvents(NewParamEventDto params);

    EventFullDto update(UpdateEventAdminRequest request, Long eventId);
}
