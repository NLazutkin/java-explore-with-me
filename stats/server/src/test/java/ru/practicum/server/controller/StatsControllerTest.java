package ru.practicum.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.exception.BadRequestException;
import ru.practicum.server.service.StatService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
class StatsControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    StatService statService;

    @Autowired
    private MockMvc mvc;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static List<ViewStatsDto> viewStatsDtoList;

    @BeforeEach
    void getViewStatsDtoList() {
        viewStatsDtoList = List.of(
                new ViewStatsDto("ewm-main-service", "/events/1", 3L),
                new ViewStatsDto("ewm-main-service", "/events/2", 6L),
                new ViewStatsDto("ewm-main-service", "/events/3", 9L)
        );
    }

    @Test
    void saveTest() throws Exception {
        EndpointHitDto endpointHitDto = new EndpointHitDto(1L,
                "ewm-main-service",
                "/events/1",
                "192.163.0.1",
                LocalDateTime.of(2024, 1, 1, 12, 35, 10));

        when(statService.save(any())).thenReturn(endpointHitDto);

        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(endpointHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id", is(endpointHitDto.getId()), Long.class))
                .andExpect(jsonPath("$.app", is(endpointHitDto.getApp()), String.class))
                .andExpect(jsonPath("$.uri", is(endpointHitDto.getUri()), String.class))
                .andExpect(jsonPath("$.ip", is(endpointHitDto.getIp()), String.class))
                .andExpect(jsonPath("$.timestamp", is(endpointHitDto.getTimestamp()
                        .format(FORMATTER)), String.class));
    }

    @Test
    void getTest() throws Exception {

        when(statService.get(any(), any(), anyList(), anyBoolean())).thenReturn(viewStatsDtoList);

        mvc.perform(get("/stats")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("start", LocalDateTime.now()
                                .minusHours(1)
                                .format(FORMATTER))
                        .param("end", LocalDateTime.now()
                                .plusHours(1)
                                .format(FORMATTER))
                        .param("uris", "/events/1, /events/2, /events/3")
                        .param("unique", "false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].app").value(is(viewStatsDtoList.get(0).getApp()), String.class))
                .andExpect(jsonPath("$[0].uri").value(is(viewStatsDtoList.get(0).getUri()), String.class))
                .andExpect(jsonPath("$[0].hits").value(is(viewStatsDtoList.get(0).getHits()), Long.class))
                .andExpect(jsonPath("$[2].app").value(is(viewStatsDtoList.get(2).getApp()), String.class))
                .andExpect(jsonPath("$[2].uri").value(is(viewStatsDtoList.get(2).getUri()), String.class))
                .andExpect(jsonPath("$[2].hits").value(is(viewStatsDtoList.get(2).getHits()), Long.class));
    }

    @Test
    void getTestWithWrongDates() throws Exception {

        when(statService.get(any(), any(), anyList(), anyBoolean())).thenReturn(viewStatsDtoList);

        mvc.perform(get("/stats")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("start", LocalDateTime.now()
                                .plusHours(1)
                                .format(FORMATTER))
                        .param("end", LocalDateTime.now()
                                .minusHours(1)
                                .format(FORMATTER))
                        .param("uris", "/events/1, /events/2, /events/3")
                        .param("unique", "false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertInstanceOf(BadRequestException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Дата начала позже даты окончания!",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void getTestWithWrongParams() throws Exception {

        when(statService.get(any(), any(), anyList(), anyBoolean())).thenReturn(viewStatsDtoList);

        mvc.perform(get("/stats")
                        .characterEncoding(StandardCharsets.UTF_8)
                        // without "start"
                        .param("end", LocalDateTime.now()
                                .minusHours(1)
                                .format(FORMATTER))
                        .param("uris", "/events/1, /events/2, /events/3")
                        .param("unique", "false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(result -> assertInstanceOf(Throwable.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Required request parameter 'start' for method parameter " +
                                "type LocalDateTime is not present",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
}