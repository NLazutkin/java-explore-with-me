package ru.practicum.server.mapper;

import ru.practicum.dto.ViewStatsDto;
import lombok.experimental.UtilityClass;
import ru.practicum.server.model.ViewStats;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ViewStatsMapper {

    public static ViewStatsDto mapToDto(ViewStats entity) {
        ViewStatsDto viewStatsDto = new ViewStatsDto();
        viewStatsDto.setApp(entity.getApp());
        viewStatsDto.setUri(entity.getUri());
        viewStatsDto.setHits(entity.getHits());

        return viewStatsDto;
    }

    public static ViewStats mapToEntity(ViewStatsDto dto) {
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(dto.getApp());
        viewStats.setUri(dto.getUri());
        viewStats.setHits(dto.getHits());

        return viewStats;
    }

    public static List<ViewStatsDto> mapToListDto(List<ViewStats> listEntity) {
        return listEntity.stream().map(ViewStatsMapper::mapToDto).collect(Collectors.toList());
    }
}
