package ru.practicum.server.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.server.model.EndpointHit;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class EndpointHitMapperTest {
    private static final String APP = "ewm-main-service";
    private static final String BASE_IP = "192.163.0.";
    private static final String BASE_URI = "/events/";

    private final EndpointHit entity = new EndpointHit(1L,
            APP,
            BASE_URI + "1",
            BASE_IP + "1",
            LocalDateTime.of(2024, 1, 1, 12, 35, 10));
    private final EndpointHitDto dto = new EndpointHitDto(1L,
            APP,
            BASE_URI + "1",
            BASE_IP + "1",
            LocalDateTime.of(2024, 1, 1, 12, 35, 10));

    @Test
    public void toEndpointHitDtoTest() {
        EndpointHitDto endpointHitDto = EndpointHitMapper.mapToDto(entity);
        assertThat(endpointHitDto, equalTo(dto));
    }

    @Test
    public void toEndpointHitTest() {
        EndpointHit endpointHit = EndpointHitMapper.mapToEntity(dto);
        assertThat(endpointHit.getApp(), equalTo(entity.getApp()));
        assertThat(endpointHit.getUri(), equalTo(entity.getUri()));
        assertThat(endpointHit.getIp(), equalTo(entity.getIp()));
        assertThat(endpointHit.getTimestamp(), equalTo(entity.getTimestamp()));
    }
}
