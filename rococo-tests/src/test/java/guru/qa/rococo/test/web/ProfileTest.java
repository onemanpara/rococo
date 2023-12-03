package guru.qa.rococo.test.web;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.jupiter.annotation.GeneratedUser;
import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.util.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.jupiter.annotation.GeneratedUser.Selector.API_LOGIN;
import static guru.qa.rococo.util.ImageUtil.convertImageToBase64;

@DisplayName("WEB: Профиль пользователя")
public class ProfileTest extends BaseWebTest {
    @Test
    @DisplayName("WEB: В профиле пользователя отображается его юзернейм")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    void shouldShowUsernameAtProfileModal(@GeneratedUser(selector = API_LOGIN) UserJson createdUser) {
        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .openProfileModal()
                .checkUsername(createdUser.getUsername());
    }

    @Test
    @DisplayName("WEB: Пользователь может обновить данные профиля")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    void shouldUpdateProfileInfo() {
        String firstName = "Иван";
        String surnameName = "Иванов";

        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .openProfileModal()
                .setFirstName(firstName)
                .setSurname(surnameName)
                .successSubmit();
        mainPage
                .checkNotificationText("Профиль обновлен")
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .openProfileModal()
                .checkFirstname(firstName)
                .checkSurname(surnameName);
    }

    @Test
    @DisplayName("WEB: Пользователь может загрузить аватар")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    void shouldUploadAvatar() {
        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .openProfileModal()
                .setAvatar(PHOTO_PATH)
                .successSubmit();
        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .checkPhoto(convertImageToBase64(PHOTO_PATH))
                .openProfileModal()
                .checkPhoto(convertImageToBase64(PHOTO_PATH));
    }

    @Test
    @DisplayName("WEB: При указании имени/фамилии длиннее 50 символов возникает ошибка")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    void shouldShowErrorIfFirstnameOrSurnameIsLargeThanMaximalCharactersCount() {
        String longWord = DataUtil.generateRandomWord(51);

        mainPage
                .openPage()
                .waitForPageIsLoaded()
                .getHeader()
                .openProfileModal()
                .setFirstName(longWord)
                .setSurname(longWord)
                .errorSubmit()
                .checkFirstnameError("Имя не может быть длиннее 50 символов")
                .checkSurnameError("Фамилия не может быть длиннее 50 символов");
    }
}
