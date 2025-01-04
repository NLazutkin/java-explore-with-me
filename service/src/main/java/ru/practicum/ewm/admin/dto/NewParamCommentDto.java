package ru.practicum.ewm.admin.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewParamCommentDto {
    String text;
    List<Long> users;
    List<Long> events;
    Boolean positive;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    Integer from;
    Integer size;
}

