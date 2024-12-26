package ru.practicum.ewm.base.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"name"})
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCategoryDto {
    @NotBlank(message = "Название добавляемой категории не может быть пустым!")
    @Length(min = 1, max = 50)
    String name;
}
