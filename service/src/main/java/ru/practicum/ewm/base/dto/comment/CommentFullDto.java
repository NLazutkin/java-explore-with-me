package ru.practicum.ewm.base.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.base.dto.user.UserDto;
import ru.practicum.ewm.base.util.DatePattern;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentFullDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    String text;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long event;

    UserDto author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DatePattern.DATE_TIME_PATTERN)
    LocalDateTime created;

    Boolean positive;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DatePattern.DATE_TIME_PATTERN)
    LocalDateTime lastModify;
}
