package ru.practicum.ewm.common.dto;

import jakarta.servlet.http.HttpServletRequest;
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
public class EventRequestParam {
    String text;
    List<Long> categories;
    Boolean paid;
    LocalDateTime rangeStart;
    LocalDateTime rangeEnd;
    Boolean onlyAvailable;
    String sort;
    Integer from;
    Integer size;
    HttpServletRequest request;

    public boolean expectedBaseCriteria() {
        return (this.text != null && this.text.equals("0"))
                && ((this.categories.size() == 1) && (this.categories.getFirst().equals(0L)));
    }
}
