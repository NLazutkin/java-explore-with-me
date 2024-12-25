package ru.practicum.ewm.common.service.category;

import ru.practicum.ewm.base.dto.category.CategoryDto;

import java.util.Collection;

public interface CommonCategoryService {
    Collection<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto get(Long catId);
}
