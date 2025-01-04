package ru.practicum.ewm.base.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentDto {
    @NotBlank(message = "Комментарий не может быть пустым")
    String text;

    Boolean positive;
}
