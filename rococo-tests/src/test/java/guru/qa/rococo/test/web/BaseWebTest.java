package guru.qa.rococo.test.web;

import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.jupiter.annotation.WebTest;
import guru.qa.rococo.page.ArtistListPage;
import guru.qa.rococo.page.MainPage;
import guru.qa.rococo.page.MuseumListPage;
import guru.qa.rococo.page.PaintingListPage;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.BeforeEach;

@WebTest
public abstract class BaseWebTest {

    protected static final Config CFG = Config.getConfig();
    protected static final String PHOTO_PATH = "files/testimg.jpg";
    protected static final String NEW_PHOTO_PATH = "files/newphoto.jpg";
    protected final MainPage mainPage = new MainPage();
    protected final MuseumListPage museumListPage = new MuseumListPage();
    protected final ArtistListPage artistListPage = new ArtistListPage();
    protected final PaintingListPage paintingListPage = new PaintingListPage();

    @BeforeEach
    void setup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(false)
                .savePageSource(false)
        );
    }

}
