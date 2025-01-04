package ru.practicum.ewm.base.dto.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.base.util.IfNotNullIsNotEmpty.IfNotNullIsNotEmpty;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCommentRequest {
    @IfNotNullIsNotEmpty(message = "Комментарий не может быть пустым")
    String text;

    Boolean positive;

    public boolean hasText() {
        return this.text != null;
    }

    public boolean hasPositive() {
        return this.positive != null;
    }
}
