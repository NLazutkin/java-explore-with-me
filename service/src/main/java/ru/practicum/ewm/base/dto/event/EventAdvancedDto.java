package ru.practicum.ewm.base.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.dto.comment.CommentShortDto;
import ru.practicum.ewm.base.dto.location.LocationDto;
import ru.practicum.ewm.base.dto.user.UserShortDto;
import ru.practicum.ewm.base.enums.States;
import ru.practicum.ewm.base.util.DatePattern;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventAdvancedDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    LocalDateTime createdOn;
    String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DatePattern.DATE_TIME_PATTERN)
    LocalDateTime eventDate;
    UserShortDto initiator;
    LocationDto location;
    Boolean paid;
    Long participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    States state;
    String title;
    Long views;
    List<CommentShortDto> comments;
}
