package ru.practicum.ewm.base.dto.location;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"lat", "lon"})
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDto {
    @NotNull(message = "Широта должна быть указана")
    Float lat;

    @NotNull(message = "Долгота должна быть указана")
    Float lon;
}
