package ru.practicum.server.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.practicum.server.mapper.DateTimeMapper;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    private String status;
    private String reason;
    private String message;
    private String timestamp;

    public ErrorResponse(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = DateTimeMapper.toStringDate(LocalDateTime.now());
    }
}
