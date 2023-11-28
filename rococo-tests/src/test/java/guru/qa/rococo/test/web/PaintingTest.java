package guru.qa.rococo.test.web;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.GenerateArtist;
import guru.qa.rococo.jupiter.annotation.GenerateMuseum;
import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.util.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.util.DataUtil.generateRandomSentence;

@DisplayName("WEB: Картины")
public class PaintingTest extends BaseWebTest {

    @Test
    @DisplayName("WEB: Пользователь может добавить новую картину")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    @GenerateMuseum
    @GenerateArtist
    void shouldAddNewPainting(MuseumJson createdMuseum, ArtistJson createdArtist) {
        String title = DataUtil.generateRandomPaintingName();
        String description = generateRandomSentence(30);

        paintingListPage
                .openPage()
                .waitForPageIsLoaded()
                .addNewPainting()
                .setTitle(title)
                .setPhoto(PHOTO_PATH)
                .selectArtist(createdArtist.getName())
                .setDescription(description)
                .selectMuseum(createdMuseum.getTitle())
                .successSubmit();
        paintingListPage
                .checkNotificationText("Добавлена картина: " + title)
                .openPage()
                .waitForPageIsLoaded()
                .checkPaintingIsExist(title);
    }

    @Test
    @DisplayName("WEB: При использовании поиска отображается уведомление об отсутствии результатов")
    @Tag("WEB")
    void shouldShowEmptySearchNotificationIfResultsAreEmpty() {
        String emptySearchQuery = generateRandomSentence(30);

        paintingListPage
                .openPage()
                .waitForPageIsLoaded()
                .filterPaintingByTitle(emptySearchQuery)
                .checkEmptySearch("Картины не найдены")
                .checkEmptySearch("Для указанного вами фильтра мы не смогли найти ни одной картины");
    }

    @Test
    @DisplayName("WEB: При указании названия и описания картины длиннее допустимого количества символов возникает ошибка")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    @GenerateMuseum
    @GenerateArtist
    void shouldShowErrorIfArtistNameAndBiographyIsLargeThanMaximalCharactersCount(MuseumJson createdMuseum, ArtistJson createdArtist) {
        String longWord = DataUtil.generateRandomWord(256);
        String veryLongWord = DataUtil.generateRandomWord(2001);

        paintingListPage
                .openPage()
                .waitForPageIsLoaded()
                .addNewPainting()
                .setTitle(longWord)
                .setPhoto(PHOTO_PATH)
                .selectArtist(createdArtist.getName())
                .setDescription(veryLongWord)
                .selectMuseum(createdMuseum.getTitle())
                .errorSubmit()
                .checkTitleError("Название не может быть длиннее 255 символов")
                .checkDescriptionError("Описание не может быть длиннее 2000 символов");
    }
}
