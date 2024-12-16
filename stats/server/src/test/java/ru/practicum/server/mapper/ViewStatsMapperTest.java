package ru.practicum.server.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.ViewStats;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ViewStatsMapperTest {
    private final ViewStats entity = new ViewStats("ewm-main-service", "/events/1", 6L);
    private final ViewStatsDto dto = new ViewStatsDto("ewm-main-service", "/events/1", 6L);

    @Test
    public void toViewStatsDtoTest() {
        ViewStatsDto viewStatsDto = ViewStatsMapper.mapToDto(entity);
        assertThat(viewStatsDto, equalTo(dto));
    }

    @Test
    public void toViewStatsTest() {
        ViewStats viewStats = ViewStatsMapper.mapToEntity(dto);
        assertThat(viewStats.getApp(), equalTo(entity.getApp()));
        assertThat(viewStats.getUri(), equalTo(entity.getUri()));
        assertThat(viewStats.getHits(), equalTo(entity.getHits()));
    }

    @Test
    public void toViewStatsListDtoTest() {
        List<ViewStats> viewStatsList = List.of(
                new ViewStats("ewm-main-service", "/events/1", 3L),
                new ViewStats("ewm-main-service", "/events/2", 6L),
                new ViewStats("ewm-main-service", "/events/3", 9L)
        );

        List<ViewStatsDto> viewStatsDtoList = List.of(
                new ViewStatsDto("ewm-main-service", "/events/1", 3L),
                new ViewStatsDto("ewm-main-service", "/events/2", 6L),
                new ViewStatsDto("ewm-main-service", "/events/3", 9L)
        );

        List<ViewStatsDto> listDto = ViewStatsMapper.mapToListDto(viewStatsList);
        assertThat(listDto, equalTo(viewStatsDtoList));
    }
}