package ru.practicum.ewm.admin.service.user;

import ru.practicum.ewm.base.dto.user.NewUserRequest;
import ru.practicum.ewm.base.dto.user.UserDto;

import java.util.Collection;
import java.util.List;

public interface AdminUserService {
    UserDto save(NewUserRequest request);

    Collection<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    void delete(Long userId);
}
