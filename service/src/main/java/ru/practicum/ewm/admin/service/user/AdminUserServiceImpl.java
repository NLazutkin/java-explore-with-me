package ru.practicum.ewm.admin.service.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.dto.user.NewUserRequest;
import ru.practicum.ewm.base.dto.user.UserDto;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.UserMapper;
import ru.practicum.ewm.base.models.User;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.repository.user.UserRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminUserServiceImpl implements AdminUserService {

    UserRepository userRepository;

    @Autowired
    public AdminUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    @Override
    @Transactional
    public UserDto save(NewUserRequest request) {
        User user = UserMapper.mapToEntity(request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(String.format("Email %s уже занят", user.getEmail()));
        }

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Сохраняем данные о пользователе {}", request.getName());
        return UserMapper.mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        List<User> users;

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        if (ids == null) {
            users = userRepository.findAll(pageRequest).toList();
        } else {
            users = userRepository.findAllByIdIn(ids, pageRequest);
        }

        log.info("Получаем данные о {} пользователях", users.size());
        return UserMapper.mapToListDto(users);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User user = findById(userId);
        userRepository.deleteById(userId);
        log.info("Пользователь {} удален", user.getName());
    }
}
