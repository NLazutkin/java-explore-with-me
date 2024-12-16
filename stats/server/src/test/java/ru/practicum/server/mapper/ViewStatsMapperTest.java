package ru.practicum.server.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.ViewStats;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ViewStatsMapperTest {
    private static final String APP = "ewm-main-service";
    private static final String BASE_URI = "/events/";

    private final ViewStats entity = new ViewStats(APP, BASE_URI + "1", 6L);
    private final ViewStatsDto dto = new ViewStatsDto(APP, BASE_URI + "1", 6L);

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
                new ViewStats(APP, BASE_URI + "1", 3L),
                new ViewStats(APP, BASE_URI + "2", 6L),
                new ViewStats(APP, BASE_URI + "3", 9L)
        );

        List<ViewStatsDto> viewStatsDtoList = List.of(
                new ViewStatsDto(APP, BASE_URI + "1", 3L),
                new ViewStatsDto(APP, BASE_URI + "2", 6L),
                new ViewStatsDto(APP, BASE_URI + "3", 9L)
        );

        List<ViewStatsDto> listDto = ViewStatsMapper.mapToListDto(viewStatsList);
        assertThat(listDto, equalTo(viewStatsDtoList));
    }
}