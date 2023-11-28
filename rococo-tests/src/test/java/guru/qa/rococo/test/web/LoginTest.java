package guru.qa.rococo.test.web;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.jupiter.annotation.GeneratedUser;
import guru.qa.rococo.model.UserJson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("WEB: Авторизация")
public class LoginTest extends BaseWebTest {

    @Test
    @DisplayName("WEB: Главная страница должна отображаться после логина новым юзером")
    @Tag("WEB")
    @GenerateUser
    void mainPageShouldBeVisibleAfterLogin(@GeneratedUser UserJson createdUser) {
        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .login()
                .waitForPageIsLoaded()
                .fillLoginPage(createdUser.getUsername(), createdUser.getPassword())
                .successSubmit()
                .waitForPageIsLoaded()
                .getHeader()
                .checkUserIsAuthorized();
    }

    @Test
    @DisplayName("WEB: При неверно введенном логине пользователь остается неавторизованным")
    @Tag("WEB")
    @GenerateUser
    void userShouldStayOnLoginPageAfterLoginWithBadUsername(@GeneratedUser UserJson createdUser) {
        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .login()
                .waitForPageIsLoaded()
                .fillLoginPage(createdUser.getUsername() + "BAD", createdUser.getPassword())
                .errorSubmit().checkError("Неверные учетные данные пользователя");
    }

    @Test
    @DisplayName("WEB: При неверно введенном пароле пользователь остается неавторизованным")
    @Tag("WEB")
    @GenerateUser
    void userShouldStayOnLoginPageAfterLoginWithBadPassword(@GeneratedUser UserJson createdUser) {
        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .login()
                .waitForPageIsLoaded()
                .fillLoginPage(createdUser.getUsername(), createdUser.getPassword() + "BAD")
                .errorSubmit()
                .checkError("Неверные учетные данные пользователя");
    }

    @Test
    @DisplayName("WEB: Пользователь может завершить сессию")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    void shouldLogout() {
        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .openProfileModal()
                .logout();
        mainPage
                .checkNotificationText("Сессия завершена")
                .getHeader()
                .checkUserIsNotAuthorized();
    }

}
