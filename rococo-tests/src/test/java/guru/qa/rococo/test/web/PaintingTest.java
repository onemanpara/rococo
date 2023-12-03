package guru.qa.rococo.test.web;

import guru.qa.rococo.jupiter.annotation.*;
import guru.qa.rococo.model.ArtistJson;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.model.PaintingJson;
import guru.qa.rococo.page.PaintingDetailPage;
import guru.qa.rococo.util.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.util.DataUtil.generateRandomPaintingName;
import static guru.qa.rococo.util.DataUtil.generateRandomSentence;
import static guru.qa.rococo.util.ImageUtil.convertImageToBase64;

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
    @DisplayName("WEB: Пользователь может изменить данные картины")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    @GeneratePainting
    @GenerateMuseum
    @GenerateArtist
    void shouldEditPainting(PaintingJson createdPainting, MuseumJson createdMuseum, ArtistJson createdArtist) {
        String newTitle = generateRandomPaintingName();
        String newDescription = DataUtil.generateRandomSentence(50);
        String newArtistName = createdArtist.getName();
        String newMuseumName = createdMuseum.getTitle();
        PaintingDetailPage paintingDetailPage = new PaintingDetailPage(createdPainting.getId().toString());

        paintingDetailPage
                .openPage()
                .waitForPageIsLoaded()
                .editPainting()
                .setPhoto(NEW_PHOTO_PATH)
                .setTitle(newTitle)
                .selectArtist(newArtistName)
                .setDescription(newDescription)
                .selectMuseum(newMuseumName)
                .successSubmit();
        paintingDetailPage
                .checkNotificationText("Обновлена картина: " + newTitle)
                .openPage()
                .waitForPageIsLoaded()
                .checkTitle(newTitle)
                .checkArtist(newArtistName)
                .checkDescription(newDescription)
                .checkPhoto(convertImageToBase64(NEW_PHOTO_PATH));
    }

    @Test
    @DisplayName("WEB: При использовании поиска отображается найденная картина")
    @Tag("WEB")
    @GeneratePainting
    void shouldShowPaintingAfterFilter(PaintingJson createdPainting) {
        paintingListPage
                .openPage()
                .waitForPageIsLoaded()
                .filterPaintingByTitle(createdPainting.getTitle())
                .checkPainitngListSize(1);
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
