package ru.practicum.ewm.base.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.dto.category.NewCategoryDto;
import ru.practicum.ewm.base.models.Category;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {
    public static Category mapToEntity(NewCategoryDto request) {
        Category category = new Category();
        category.setName(request.getName());

        return category;
    }

    public static CategoryDto mapToDto(Category entity) {
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    public static Category updateFields(Category entity, NewCategoryDto request) {
        entity.setName(request.getName());
        return entity;
    }


    public static List<CategoryDto> mapToListDto(List<Category> listEntity) {
        return listEntity.stream().map(CategoryMapper::mapToDto).collect(Collectors.toList());
    }
}
