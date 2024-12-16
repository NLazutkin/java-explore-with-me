package ru.practicum.server.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.dto.ViewStatsDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ViewStatsDtoJsonTest {

    @Autowired
    private JacksonTester<ViewStatsDto> json;

    @Test
    public void testEndpointHitDto() throws Exception {
        ViewStatsDto viewStatsDto = new ViewStatsDto("ewm-main-service", "/events/1", 6L);

        JsonContent<ViewStatsDto> result = json.write(viewStatsDto);

        assertThat(result).extractingJsonPathStringValue("$.app").isEqualTo(viewStatsDto.getApp());
        assertThat(result).extractingJsonPathStringValue("$.uri").isEqualTo(viewStatsDto.getUri());
        assertThat(result).extractingJsonPathNumberValue("$.hits").isEqualTo(6);
    }
}