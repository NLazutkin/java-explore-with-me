package ru.practicum.ewm.common.service.category;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.CategoryMapper;
import ru.practicum.ewm.base.models.Category;
import ru.practicum.ewm.base.repository.category.CategoryRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommonCategoryServiceImpl implements CommonCategoryService {
    CategoryRepository categoryRepository;

    public CommonCategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория c ID %d не найдена", catId)));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<CategoryDto> getAll(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id"));

        List<Category> test = categoryRepository.findAll(pageRequest).toList();

        return CategoryMapper.mapToListDto(test);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto get(Long catId) {
        return CategoryMapper.mapToDto(findById(catId));
    }
}
