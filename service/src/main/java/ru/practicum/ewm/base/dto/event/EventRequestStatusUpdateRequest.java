package ru.practicum.ewm.base.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    @NotNull(message = "При обновлении, ID запросов должны существовать")
    List<Long> requestIds;

    @NotBlank(message = "Статус обновляемых запросов должен существовать и быть не пустым")
    String status;
}
