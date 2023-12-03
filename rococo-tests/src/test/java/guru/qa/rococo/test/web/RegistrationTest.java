package guru.qa.rococo.test.web;

import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.jupiter.annotation.GeneratedUser;
import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.util.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("WEB: Регистрация")
public class RegistrationTest extends BaseWebTest {

    @Test
    @DisplayName("WEB: Пользователь может успешно зарегистрироваться в системе")
    @Tag("WEB")
    void shouldRegisterNewUser() {
        String username = DataUtil.generateRandomUsername();
        String password = DataUtil.generateRandomPassword();

        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .login()
                .waitForPageIsLoaded()
                .register()
                .waitForPageIsLoaded()
                .fillRegistrationPage(username, password, password).successSubmit()
                .login().waitForPageIsLoaded()
                .fillLoginPage(username, password)
                .successSubmit()
                .waitForPageIsLoaded()
                .getHeader()
                .checkUserIsAuthorized();
    }

    @Test
    @DisplayName("WEB: При регистрации возникает ошибка, если указывается юзернейм, который занят")
    @Tag("WEB")
    @GenerateUser
    void shouldNotRegisterUserWithExistingUsername(@GeneratedUser UserJson createdUser) {
        String username = createdUser.getUsername();
        String password = DataUtil.generateRandomPassword();

        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .login()
                .waitForPageIsLoaded()
                .register()
                .fillRegistrationPage(username, password, password)
                .errorSubmit()
                .checkUsernameError("Имя пользователя `" + username + "` занято");
    }

    @Test
    @DisplayName("WEB: При регистрации возникает ошибка, если указываются разные пароль и подтверждение пароля")
    @Tag("WEB")
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        String username = DataUtil.generateRandomUsername();
        String password = DataUtil.generateRandomPassword();
        String confirmPassword = DataUtil.generateRandomPassword();

        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .login()
                .waitForPageIsLoaded()
                .register()
                .fillRegistrationPage(username, password, confirmPassword)
                .errorSubmit()
                .checkPasswordError("Пароли должны быть одинаковыми");
    }

    @Test
    @DisplayName("WEB: При регистрации возникает ошибка, если указанный юзернейм длиннее 50 символов")
    @Tag("WEB")
    void shouldShowErrorIfUsernameIsLargeThanMaximalCharactersCount() {
        String longWord = DataUtil.generateRandomWord(51);
        String password = DataUtil.generateRandomPassword();

        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .login()
                .waitForPageIsLoaded()
                .register()
                .fillRegistrationPage(longWord, password, password)
                .errorSubmit()
                .checkUsernameError("Длина имени пользователя не должна превышать 50 символов");
    }

    @Test
    @DisplayName("WEB: При регистрации возникает ошибка, если указанный пароль короче 5 символов")
    @Tag("WEB")
    void shouldShowErrorIfPasswordIsLessThanMinimalCharactersCount() {
        String username = DataUtil.generateRandomUsername();
        String password = DataUtil.generateRandomPassword(1, 4);

        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .login()
                .waitForPageIsLoaded()
                .register()
                .fillRegistrationPage(username, password, password)
                .errorSubmit()
                .checkPasswordError("Допустимая длина пароля от 5 до 12 символов");
    }

    @Test
    @DisplayName("WEB: При регистрации возникает ошибка, если указанный пароль длиннее 12 символов")
    @Tag("WEB")
    void shouldShowErrorIfPasswordIsLargeThanMaximalCharactersCount() {
        String username = DataUtil.generateRandomUsername();
        String password = DataUtil.generateRandomPassword(13, 14);

        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .login()
                .waitForPageIsLoaded()
                .register()
                .fillRegistrationPage(username, password, password)
                .errorSubmit()
                .checkPasswordError("Допустимая длина пароля от 5 до 12 символов");
    }

}

