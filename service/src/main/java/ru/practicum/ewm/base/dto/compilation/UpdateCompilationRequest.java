package ru.practicum.ewm.base.dto.compilation;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.base.util.IfNotNullIsNotEmpty.IfNotNullIsNotEmpty;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationRequest {
    Set<Long> events;

    Boolean pinned;

    @IfNotNullIsNotEmpty
    @Length(min = 1, max = 50)
    String title;

    public boolean hasEvents() {
        return this.pinned != null;
    }

    public boolean hasPinned() {
        return this.pinned != null;
    }

    public boolean hasTitle() {
        return this.title != null;
    }
}
