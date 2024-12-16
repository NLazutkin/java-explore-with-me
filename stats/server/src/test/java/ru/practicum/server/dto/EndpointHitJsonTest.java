package ru.practicum.server.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class EndpointHitJsonTest {

    @Autowired
    private JacksonTester<EndpointHitDto> json;

    @Test
    public void testEndpointHitDto() throws Exception {
        EndpointHitDto endpointHitDto = new EndpointHitDto(1L,
                "ewm-main-service",
                "/events/1",
                "192.163.0.1",
                LocalDateTime.of(2024, 1, 1, 12, 35, 10));

        JsonContent<EndpointHitDto> result = json.write(endpointHitDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(endpointHitDto.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(endpointHitDto.getUri());
        assertThat(result).extractingJsonPathStringValue("$.ip").isEqualTo(endpointHitDto.getIp());
        assertThat(result).extractingJsonPathStringValue("$.timestamp")
                .isEqualTo(endpointHitDto.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
