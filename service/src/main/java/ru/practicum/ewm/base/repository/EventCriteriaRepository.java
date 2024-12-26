package ru.practicum.ewm.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.admin.dto.NewParamEventDto;
import ru.practicum.ewm.base.models.Event;
import ru.practicum.ewm.base.models.EventCriteria;

@Repository
public interface EventCriteriaRepository {
    Page<Event> findAllWithCriteria(PageRequest pageRequest, EventCriteria eventSearchCriteria);

    Page<Event> findAllWithCriteria(PageRequest pageRequest, NewParamEventDto newParamEventDto);

}
