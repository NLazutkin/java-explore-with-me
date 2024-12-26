package ru.practicum.ewm.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.base.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}