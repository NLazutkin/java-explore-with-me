package ru.practicum.server.mapper;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DateTimeMapperTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime localDateTimeValue = LocalDateTime.now();
    private final String stringValue = LocalDateTime.now().format(FORMATTER);

    @Test
    public void toLocalDateTimeTest() {
        LocalDateTime localDateTime = DateTimeMapper.toLocalDateTime(stringValue);
        assertThat(localDateTime, equalTo(LocalDateTime.parse(stringValue, FORMATTER)));
    }

    @Test
    public void toStringDateTest() {
        String stringDate = DateTimeMapper.toStringDate(localDateTimeValue);
        assertThat(stringDate, equalTo(stringValue));
    }
}
