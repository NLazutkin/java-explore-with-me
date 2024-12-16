package ru.practicum.server.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=stats",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class StatsServerServiceIntegrationTest {
    private final EntityManager em;
    private final StatService statService;

    private EndpointHitDto makeEndpointHit(String app, String uri, String ip, LocalDateTime timestamp) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(app);
        endpointHitDto.setUri(uri);
        endpointHitDto.setIp(ip);
        endpointHitDto.setTimestamp(timestamp);

        return endpointHitDto;
    }

    @Test
    void saveHitTest() {
        EndpointHitDto endpointHitDto = makeEndpointHit("ewm-main-service",
                "/events/1",
                "192.163.0.1",
                LocalDateTime.of(2024, 1, 1, 12, 35, 10));

        statService.save(endpointHitDto);

        TypedQuery<EndpointHit> query = em.createQuery("SELECT h FROM EndpointHit h WHERE h.ip LIKE :ip", EndpointHit.class);
        EndpointHit endpointHit = query.setParameter("ip", endpointHitDto.getIp()).getSingleResult();

        MatcherAssert.assertThat(endpointHit.getId(), CoreMatchers.notNullValue());
        MatcherAssert.assertThat(endpointHit.getApp(), Matchers.equalTo(endpointHitDto.getApp()));
        MatcherAssert.assertThat(endpointHit.getUri(), Matchers.equalTo(endpointHitDto.getUri()));
        MatcherAssert.assertThat(endpointHit.getIp(), Matchers.equalTo(endpointHitDto.getIp()));
        MatcherAssert.assertThat(endpointHit.getTimestamp(), Matchers.equalTo(endpointHitDto.getTimestamp()));
    }

    @Test
    void getHitsBetweenDatesTest() {
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 1, 12, 15, 10);
        List<EndpointHitDto> newHits = List.of(
                makeEndpointHit("ewm-main-service",
                        "/events/1",
                        "192.163.0.1",
                        timestamp),
                makeEndpointHit("ewm-main-service",
                        "/events/1",
                        "192.163.0.1",
                        timestamp.plusMinutes(10)),
                makeEndpointHit("ewm-main-service",
                        "/events/3",
                        "192.163.0.3",
                        timestamp.plusMinutes(20)));

        for (EndpointHitDto hit : newHits) {
            statService.save(hit);
        }

        List<ViewStatsDto> loadHits = statService.get(timestamp.minusMinutes(10),
                timestamp.plusMinutes(30),
                List.of("/events/1", "/events/2", "/events/3"),
                Boolean.FALSE);

        assertThat(loadHits, hasSize(2));
        MatcherAssert.assertThat(loadHits.get(0).getApp(),  CoreMatchers.equalTo(newHits.get(0).getApp()));
        MatcherAssert.assertThat(loadHits.get(0).getUri(),  CoreMatchers.equalTo(newHits.get(0).getUri()));
        MatcherAssert.assertThat(loadHits.get(0).getHits(), CoreMatchers.equalTo(2L));
        MatcherAssert.assertThat(loadHits.get(1).getApp(),  CoreMatchers.equalTo(newHits.get(2).getApp()));
        MatcherAssert.assertThat(loadHits.get(1).getUri(),  CoreMatchers.equalTo(newHits.get(2).getUri()));
        MatcherAssert.assertThat(loadHits.get(1).getHits(), CoreMatchers.equalTo(1L));
    }

    @Test
    void getUniqueHitsBetweenDatesTest() {
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 1, 12, 15, 10);
        List<EndpointHitDto> newHits = List.of(
                makeEndpointHit("ewm-main-service",
                        "/events/1",
                        "192.163.0.1",
                        timestamp),
                makeEndpointHit("ewm-main-service",
                        "/events/1",
                        "192.163.0.1",
                        timestamp.plusMinutes(10)),
                makeEndpointHit("ewm-main-service",
                        "/events/3",
                        "192.163.0.3",
                        timestamp.plusMinutes(20)));

        for (EndpointHitDto hit : newHits) {
            statService.save(hit);
        }

        List<ViewStatsDto> loadHits = statService.get(timestamp.minusMinutes(10),
                timestamp.plusMinutes(30),
                List.of("/events/1", "/events/2", "/events/3"),
                Boolean.TRUE);

        assertThat(loadHits, hasSize(2));
        MatcherAssert.assertThat(loadHits.get(0).getApp(),  CoreMatchers.equalTo(newHits.get(0).getApp()));
        MatcherAssert.assertThat(loadHits.get(0).getUri(),  CoreMatchers.equalTo(newHits.get(0).getUri()));
        MatcherAssert.assertThat(loadHits.get(0).getHits(), CoreMatchers.equalTo(1L));
        MatcherAssert.assertThat(loadHits.get(1).getApp(),  CoreMatchers.equalTo(newHits.get(2).getApp()));
        MatcherAssert.assertThat(loadHits.get(1).getUri(),  CoreMatchers.equalTo(newHits.get(2).getUri()));
        MatcherAssert.assertThat(loadHits.get(1).getHits(), CoreMatchers.equalTo(1L));
    }
}
