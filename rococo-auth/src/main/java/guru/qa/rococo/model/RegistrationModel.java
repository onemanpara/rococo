package guru.qa.rococo.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@EqualPasswords
public record RegistrationModel(
        @NotNull(message = "Имя пользователя не может быть пустым")
        @NotEmpty(message = "Имя пользователя не может быть пустым")
        @Size(max = 50, message = "Длина имени пользователя не должна превышать 50 символов")
        String username,
        @NotNull(message = "Пароль не может быть пустым")
        @Size(min = 5, max = 12, message = "Допустимая длина пароля от 5 до 12 символов")
        String password,
        @NotNull(message = "Подтверждение пароля не может быть пустым")
        String passwordSubmit
) {
}
