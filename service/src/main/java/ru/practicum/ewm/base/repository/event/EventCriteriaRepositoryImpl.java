package ru.practicum.ewm.base.repository.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.admin.dto.NewParamEventDto;
import ru.practicum.ewm.base.models.Category;
import ru.practicum.ewm.base.models.Event;
import ru.practicum.ewm.base.models.EventCriteria;
import ru.practicum.ewm.base.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventCriteriaRepositoryImpl implements EventCriteriaRepository {
    private final EntityManager entityManager;

    private final CriteriaBuilder criteriaBuilder;

    public EventCriteriaRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    private Predicate getPredicates(EventCriteria criteria, Root<Event> eventRoot) {
        List<Predicate> predicates = new ArrayList<>();
        Predicate annotationPredicate = null;

        // predicates.add(criteriaBuilder.equal(eventRoot.get("state"), States.PUBLISHED.toString()));

        if (criteria.getText() != null) {
            annotationPredicate = criteriaBuilder.like(criteriaBuilder.lower(eventRoot.get("annotation")),
                    "%" + criteria.getText().toLowerCase() + "%");
        }

        if (criteria.getText() != null) {
            if (annotationPredicate == null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(eventRoot.get("description")),
                        "%" + criteria.getText().toLowerCase() + "%"));
            } else {
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(eventRoot.get("description")),
                        "%" + criteria.getText().toLowerCase() + "%");
                predicates.add(criteriaBuilder.or(annotationPredicate, descriptionPredicate));

            }
        }

        if (criteria.getCategories() != null && !criteria.getCategories().isEmpty()) {
            Join<Event, Category> categoryJoin = eventRoot.join("category");
            predicates.add(categoryJoin.get("id").in(criteria.getCategories()));
        }

        if (criteria.getPaid() != null && !criteria.getPaid().equals(Boolean.TRUE)) {
            predicates.add(criteriaBuilder.equal(eventRoot.get("paid"), criteria.getPaid()));
        }

        if (criteria.getRangeStart() != null || criteria.getRangeEnd() != null) {
            LocalDateTime rangeStart = criteria.getRangeStart() != null ? criteria.getRangeStart() : LocalDateTime.MIN;

            LocalDateTime rangeEnd = criteria.getRangeEnd() != null ? criteria.getRangeEnd() : LocalDateTime.MAX;

            predicates.add(criteriaBuilder.between(eventRoot.get("eventDate"), rangeStart, rangeEnd));
        } else {
            predicates.add(criteriaBuilder.between(eventRoot.get("eventDate"),
                    LocalDateTime.of(1900, 1, 1, 0, 0, 0),
                    LocalDateTime.of(9999, 12, 31, 23, 59, 59)));
        }

        return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
    }

    @Override
    public Page<Event> findAllWithCriteria(PageRequest pageRequest, EventCriteria criteria) {
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        Predicate predicate = getPredicates(criteria, eventRoot);
        criteriaQuery.where(predicate);

        if (pageRequest.getSort().isUnsorted()) {
            criteriaQuery.orderBy(criteriaBuilder.desc(eventRoot.get("createdOn")));
        }

        TypedQuery<Event> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageRequest.getPageSize() * pageRequest.getPageNumber());
        query.setMaxResults(pageRequest.getPageSize());

        List<Event> events = query.getResultList();

        return new PageImpl<>(events);
    }

    private Predicate getPredicates(NewParamEventDto params, Root<Event> eventRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (params.getUsers() != null && !params.getUsers().isEmpty()) {
            Join<Event, User> initiatorJoin = eventRoot.join("initiator");
            predicates.add(initiatorJoin.get("id").in(params.getUsers()));
        }

        if (params.getStates() != null && !params.getStates().isEmpty()) {
            predicates.add(eventRoot.get("state").in(params.getStates()));
        }

        if (params.getCategories() != null && !params.getCategories().isEmpty()) {
            Join<Event, Category> categoryJoin = eventRoot.join("category");
            predicates.add(categoryJoin.get("id").in(params.getCategories()));
        }

        if (params.getRangeStart() != null || params.getRangeEnd() != null) {
            LocalDateTime rangeStart = params.getRangeStart() != null ? params.getRangeStart() : LocalDateTime.MIN;

            LocalDateTime rangeEnd = params.getRangeEnd() != null ? params.getRangeEnd() : LocalDateTime.MAX;

            predicates.add(criteriaBuilder.between(eventRoot.get("eventDate"), rangeStart, rangeEnd));
        } else {
            predicates.add(criteriaBuilder.between(eventRoot.get("eventDate"),
                    LocalDateTime.of(1900, 1, 1, 0, 0, 0),
                    LocalDateTime.of(9999, 12, 31, 23, 59, 59)));
        }

        return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
    }

    @Override
    public Page<Event> findAllWithCriteria(PageRequest pageRequest, NewParamEventDto params) {
        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        Predicate predicate = getPredicates(params, eventRoot);
        criteriaQuery.where(predicate);

        if (pageRequest.getSort().isUnsorted()) {
            criteriaQuery.orderBy(criteriaBuilder.desc(eventRoot.get("createdOn")));
        }

        TypedQuery<Event> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(pageRequest.getPageSize() * pageRequest.getPageNumber());
        query.setMaxResults(pageRequest.getPageSize());

        List<Event> events = query.getResultList();

        return new PageImpl<>(events);
    }
}
