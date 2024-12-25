package ru.practicum.ewm.admin.service.category;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.base.dto.category.NewCategoryDto;
import ru.practicum.ewm.base.exceptions.ConditionsNotMetException;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.CategoryMapper;
import ru.practicum.ewm.base.models.Category;
import ru.practicum.ewm.base.repository.CategoryRepository;
import ru.practicum.ewm.base.repository.EventRepository;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminCategoryServiceImpl implements AdminCategoryService {

    CategoryRepository categoryRepository;
    EventRepository eventRepository;


    @Autowired
    public AdminCategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория c ID %d не найдена", categoryId)));
    }

    @Override
    @Transactional
    public CategoryDto save(NewCategoryDto request) {
        Category category = CategoryMapper.mapToEntity(request);
        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(String.format("Категория \"%s\" уже существует", category.getName()), e);
        }
        log.info("Сохраняем данные о категории {}", request.getName());
        return CategoryMapper.mapToDto(category);
    }

    @Override
    @Transactional
    public CategoryDto update(NewCategoryDto request, Long catId) {
        Category updatedCategory = CategoryMapper.updateFields(findById(catId), request);

        updatedCategory = categoryRepository.save(updatedCategory);

        log.info("Обновляем категорию \"{}\"", updatedCategory.getName());
        return CategoryMapper.mapToDto(updatedCategory);
    }

    @Override
    @Transactional
    public void delete(Long categoryId) {
        Category category = findById(categoryId);
        if (eventRepository.existsByCategory(category)) {
            throw new ConditionsNotMetException(String.format("Существуют события связанные с категорией %s, " +
                    "удаление категории невозможно!", category.getName()));
        } else {
            categoryRepository.deleteById(categoryId);
            log.info("Категория \"{}\" удалена", category.getName());
        }
    }
}
