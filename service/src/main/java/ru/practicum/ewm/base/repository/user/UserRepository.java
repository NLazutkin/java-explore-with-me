package ru.practicum.ewm.base.repository.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.base.models.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIdIn(List<Long> ids, PageRequest pageRequest);

    Boolean existsByEmail(String email);
}
