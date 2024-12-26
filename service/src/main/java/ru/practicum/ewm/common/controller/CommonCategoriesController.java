package ru.practicum.ewm.common.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.category.CategoryDto;
import ru.practicum.ewm.common.service.category.CommonCategoryService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CommonCategoriesController {
    private static final String M_CAT_ID = "/{cat-id}";
    private static final String PV_CAT_ID = "cat-id";

    private final CommonCategoryService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CategoryDto> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /categories с параметрами from = {}, size = {}", from, size);
        return service.getAll(from, size);
    }

    @GetMapping(M_CAT_ID)
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto get(@PathVariable(PV_CAT_ID) Long catId) {
        log.info("Получен запрос GET /categories/{}", catId);
        return service.get(catId);
    }
}
