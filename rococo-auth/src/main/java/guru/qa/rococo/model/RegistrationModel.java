package guru.qa.rococo.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@EqualPasswords
public class RegistrationModel {

    @NotNull(message = "Имя пользователя не может быть пустым")
    @NotEmpty(message = "Имя пользователя не может быть пустым")
    @Size(max = 50, message = "Длина имени пользователя не должна превышать 50 символов")
    private String username;
    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 5, max = 12, message = "Допустимая длина пароля от 5 до 12 символов")
    private String password;
    @NotNull(message = "Подтверждение пароля не может быть пустым")
    private String passwordSubmit;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSubmit() {
        return passwordSubmit;
    }

    public void setPasswordSubmit(String passwordSubmit) {
        this.passwordSubmit = passwordSubmit;
    }

}
