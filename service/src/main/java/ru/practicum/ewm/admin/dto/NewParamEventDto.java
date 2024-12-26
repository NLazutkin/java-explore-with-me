package ru.practicum.ewm.admin.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.base.enums.States;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewParamEventDto {
    List<Long> users;
    List<States> states;
    List<Long> categories;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    Integer from;
    Integer size;
}
