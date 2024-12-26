package ru.practicum.ewm.base.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.base.dto.user.NewUserRequest;
import ru.practicum.ewm.base.dto.user.UserDto;
import ru.practicum.ewm.base.dto.user.UserShortDto;
import ru.practicum.ewm.base.models.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public static User mapToEntity(NewUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());

        return user;
    }

    public static UserDto mapToDto(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());

        return dto;
    }

    public static UserShortDto mapToShortDto(User entity) {
        UserShortDto dto = new UserShortDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    public static List<UserDto> mapToListDto(List<User> listEntity) {
        return listEntity.stream().map(UserMapper::mapToDto).collect(Collectors.toList());
    }
}
