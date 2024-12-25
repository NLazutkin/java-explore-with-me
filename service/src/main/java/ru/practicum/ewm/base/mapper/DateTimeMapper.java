package ru.practicum.ewm.base.mapper;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class DateTimeMapper {

    public static LocalDateTime toLocalDateTime(String date) {
        return LocalDateTime.parse(date);
    }

    public static String toStringDate(LocalDateTime date) {
        return date.toString();
    }
}
