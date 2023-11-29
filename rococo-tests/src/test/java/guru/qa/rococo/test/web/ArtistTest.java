package guru.qa.rococo.test.web;


import guru.qa.rococo.jupiter.annotation.*;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.model.PaintingJson;
import guru.qa.rococo.page.ArtistDetailPage;
import guru.qa.rococo.util.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static guru.qa.rococo.util.DataUtil.*;
import static guru.qa.rococo.util.ImageUtil.convertImageToBase64;

@DisplayName("WEB: Художники")
public class ArtistTest extends BaseWebTest {

    @Test
    @DisplayName("WEB: Пользователь может добавить нового художника")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    void shouldAddNewArtist() {
        String name = generateRandomName();
        String biography = generateRandomSentence(30);

        artistListPage
                .openPage()
                .waitForPageIsLoaded()
                .addNewArtist()
                .setName(name)
                .setBiography(biography)
                .setPhoto(PHOTO_PATH)
                .successSubmit();
        artistListPage
                .checkNotificationText("Добавлен художник: " + name)
                .openPage()
                .waitForPageIsLoaded()
                .filterArtistByName(name)
                .checkArtistIsExist(name);
    }

    @Test
    @DisplayName("WEB: Пользователь может изменить данные о художнике")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    @GenerateArtist(name = "Художник Василий", biography = "Простой русский парень, замечательно рисует")
    void shouldEditArtist(ArtistJson createdArtist) {
        String newName = generateRandomName();
        String newBiography = DataUtil.generateRandomSentence(30);
        ArtistDetailPage artistDetailPage = new ArtistDetailPage(createdArtist.getId().toString());

        artistDetailPage
                .openPage()
                .waitForPageIsLoaded()
                .editArtist()
                .setName(newName)
                .setBiography(newBiography)
                .setPhoto(NEW_PHOTO_PATH)
                .successSubmit();
        artistDetailPage
                .checkNotificationText("Обновлен художник: " + newName)
                .openPage()
                .waitForPageIsLoaded()
                .checkName(newName)
                .checkBiography(newBiography)
                .checkPhoto(convertImageToBase64(NEW_PHOTO_PATH));
    }

    @Test
    @DisplayName("WEB: На детальной странице художника отображается картина художника")
    @Tag("WEB")
    @GeneratePainting
    void shouldShowArtistDataFromDB(PaintingJson paintingJson) {
        UUID artistId = paintingJson.getArtist().getId();
        ArtistDetailPage artistDetailPage = new ArtistDetailPage(artistId.toString());

        artistDetailPage
                .openPage()
                .waitForPageIsLoaded()
                .openPaintingCard(paintingJson.getTitle())
                .waitForPageIsLoaded();
    }

    @Test
    @DisplayName("WEB: Пользователь может добавить картину со страницы художника. " +
            "Художник автоматически предустановлен, если картина добавляется с его детальной страницы")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    @GenerateMuseum
    @GenerateArtist
    void shouldAddNewPainting(MuseumJson createdMuseum, ArtistJson createdArtist) {
        String title = DataUtil.generateRandomPaintingName();
        String description = generateRandomSentence(30);

        ArtistDetailPage artistDetailPage = new ArtistDetailPage(createdArtist.getId().toString());
        artistDetailPage
                .openPage()
                .waitForPageIsLoaded()
                .addNewPainting()
                .checkArtistSelectIsNotExist()
                .setTitle(title)
                .setPhoto(PHOTO_PATH)
                .setDescription(description)
                .selectMuseum(createdMuseum.getTitle())
                .successSubmit();
        artistDetailPage
                .checkNotificationText("Добавлена картина: " + title)
                .openPage()
                .waitForPageIsLoaded()
                .openPaintingCard(title)
                .waitForPageIsLoaded();
    }

    @Test
    @DisplayName("WEB: При использовании поиска отображается найденный художник")
    @Tag("WEB")
    @GenerateArtist
    void shouldShowMuseumAfterFilter(ArtistJson createdArtist) {
        artistListPage
                .openPage()
                .waitForPageIsLoaded()
                .filterArtistByName(createdArtist.getName())
                .checkArtistListSize(1);
    }

    @Test
    @DisplayName("WEB: При использовании поиска отображается уведомление об отсутствии результатов")
    @Tag("WEB")
    void shouldShowEmptySearchNotificationIfResultsAreEmpty() {
        String emptySearchQuery = generateRandomSentence(30);

        artistListPage
                .openPage()
                .waitForPageIsLoaded()
                .filterArtistByName(emptySearchQuery)
                .checkEmptySearch("Художники не найдены")
                .checkEmptySearch("Для указанного вами фильтра мы не смогли найти ни одного художника");
    }

    @Test
    @DisplayName("WEB: При указании имени и биографии художника длиннее допустимого количества символов возникает ошибка")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    void shouldShowErrorIfArtistNameAndBiographyIsLargeThanMaximalCharactersCount() {
        String longWord = generateRandomWord(51);
        String veryLongWord = generateRandomWord(2001);

        artistListPage
                .openPage()
                .waitForPageIsLoaded()
                .addNewArtist()
                .setName(longWord)
                .setPhoto(PHOTO_PATH)
                .setBiography(veryLongWord)
                .errorSubmit()
                .checkNameError("Имя не может быть длиннее 50 символов")
                .checkBiographyError("Биография не может быть длиннее 2000 символов");
    }
}

