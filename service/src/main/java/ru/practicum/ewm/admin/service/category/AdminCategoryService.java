package ru.practicum.ewm.admin.service.category;

import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.dto.category.NewCategoryDto;
import ru.practicum.ewm.base.models.Category;

public interface AdminCategoryService {
    Category findById(Long catId);

    CategoryDto save(NewCategoryDto request);

    CategoryDto update(NewCategoryDto request, Long catId);

    void delete(Long userId);
}
