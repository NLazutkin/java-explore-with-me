package ru.practicum.ewm.base.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.base.models.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    boolean existsByEventIdAndRequesterId(Long eventId, Long userId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long userId);
}