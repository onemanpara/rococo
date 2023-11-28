package guru.qa.rococo.test.web;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.GenerateMuseum;
import guru.qa.rococo.jupiter.annotation.GenerateUser;
import guru.qa.rococo.model.GeoJson;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.page.MuseumDetailPage;
import guru.qa.rococo.util.DataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.util.DataUtil.generateRandomMuseumName;
import static guru.qa.rococo.util.ImageUtil.convertImageToBase64;

@DisplayName("WEB: Музеи")
public class MuseumTest extends BaseWebTest {

    @Test
    @DisplayName("WEB: Пользователь может добавить новый музей")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    void shouldAddNewMuseum() {
        String title = generateRandomMuseumName();
        String country = DataUtil.getRandomCountry().name();
        String city = DataUtil.getRandomCityName();
        String description = DataUtil.generateRandomSentence(30);

        museumListPage
                .openPage()
                .waitForPageIsLoaded()
                .addNewMuseum()
                .setTitle(title)
                .selectCountry(country)
                .setCity(city)
                .setPhoto(PHOTO_PATH)
                .setDescription(description)
                .successSubmit();
        museumListPage
                .checkNotificationText("Добавлен музей: " + title)
                .openPage()
                .waitForPageIsLoaded()
                .filterMuseumsByTitle(title)
                .checkMuseumIsExist(title, city, country);
    }

    @Test
    @DisplayName("WEB: На детальной странице музея отображаются данные из БД")
    @Tag("WEB")
    @GenerateMuseum
    void shouldShowMuseumDataFromDB(MuseumJson createdMuseum) {
        String museumTitle = createdMuseum.getTitle();
        GeoJson geo = createdMuseum.getGeo();

        museumListPage
                .openPage()
                .waitForPageIsLoaded()
                .filterMuseumsByTitle(museumTitle)
                .openMuseumCard(museumTitle)
                .checkTitle(museumTitle)
                .checkGeo(geo.getCountry().name(), geo.getCity())
                .checkDescription(createdMuseum.getDescription())
                .checkPhoto(createdMuseum.getPhoto());
    }

    @Test
    @DisplayName("WEB: Пользователь может изменить данные о музее")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    @GenerateMuseum(title = "Музей в Австралии", country = "Австралия", city = "Сидней")
    void shouldEditMuseum(MuseumJson createdMuseum) {
        String newTitle = generateRandomMuseumName();
        String newCountry = "Австрия";
        String newCity = "Вена";
        String newDescription = DataUtil.generateRandomSentence(50);
        MuseumDetailPage museumDetailPage = new MuseumDetailPage(createdMuseum.getId().toString());

        museumDetailPage
                .openPage()
                .waitForPageIsLoaded()
                .editMuseum()
                .setTitle(newTitle)
                .selectCountry(newCountry)
                .setCity(newCity)
                .setDescription(newDescription)
                .setPhoto(NEW_PHOTO_PATH)
                .successSubmit();
        museumDetailPage
                .checkNotificationText("Обновлен музей: " + newTitle)
                .openPage()
                .waitForPageIsLoaded()
                .checkTitle(newTitle)
                .checkGeo(newCountry, newCity)
                .checkDescription(newDescription)
                .checkPhoto(convertImageToBase64(NEW_PHOTO_PATH));
    }

    @Test
    @DisplayName("WEB: При использовании поиска отображается найденный музей")
    @Tag("WEB")
    @GenerateMuseum
    void shouldShowMuseumAfterFilter(MuseumJson createdMuseum) {
        museumListPage
                .openPage()
                .waitForPageIsLoaded()
                .filterMuseumsByTitle(createdMuseum.getTitle())
                .checkMuseumListSize(1);
    }

    @Test
    @DisplayName("WEB: При использовании поиска отображается уведомление об отсутствии результатов")
    @Tag("WEB")
    void shouldShowEmptySearchNotificationIfResultsAreEmpty() {
        String emptySearchQuery = DataUtil.generateRandomSentence(30);

        museumListPage
                .openPage()
                .waitForPageIsLoaded()
                .filterMuseumsByTitle(emptySearchQuery)
                .checkEmptySearch("Музеи не найдены")
                .checkEmptySearch("Для указанного вами фильтра мы не смогли найти ни одного музея");
    }

    @Test
    @DisplayName("WEB: При указании названия, города и описания музея длиннее допустимого количества символов возникает ошибка")
    @Tag("WEB")
    @ApiLogin(user = @GenerateUser)
    void shouldShowErrorIfMuseumNameCityAndDescriptionIsLargeThanMaximalCharactersCount() {
        String longWord = DataUtil.generateRandomWord(256);
        String veryLongWord = DataUtil.generateRandomWord(2001);

        museumListPage
                .openPage()
                .waitForPageIsLoaded()
                .addNewMuseum()
                .setTitle(longWord)
                .selectCountry("Австралия")
                .setCity(longWord)
                .setPhoto(PHOTO_PATH)
                .setDescription(veryLongWord).errorSubmit()
                .checkTitleError("Название не может быть длиннее 255 символов")
                .checkCityError("Город не может быть длиннее 255 символов")
                .checkDescriptionError("Описание не может быть длиннее 2000 символов");
    }
}
