package ru.practicum.ewm.base.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {
    @NotBlank(message = "E-mail должен быть указан")
    @Email(message = "E-mail должен быть в формате name@somedomain.ru")
    @Length(min = 6, max = 254)
    String email;

    @NotBlank(message = "Имя пользователя должно быть указано")
    @Length(min = 2, max = 250)
    String name;
}
