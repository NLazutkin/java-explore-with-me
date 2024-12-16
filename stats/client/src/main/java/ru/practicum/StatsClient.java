package ru.practicum;

import ru.practicum.dto.EndpointHitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StatsClient extends BaseClient {
    @Autowired
    public StatsClient(@Value("${stats.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of("start", start,
                "end", end,
                "uris", uris,
                "unique", unique);

        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> save(EndpointHitDto requestDto) {
        return post("/hit", null, requestDto);
    }
}
