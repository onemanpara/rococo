package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.modal.PaintingModal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class PaintingDetailPage extends BasePage<PaintingDetailPage> {
    private static final String PAGE_URL = CFG.rococoFrontendUrl() + "/painting";
    private final String paintingId;
    private final SelenideElement title = $(".card-header");
    private final SelenideElement artist = $("[data-testid=artist]");
    private final SelenideElement description = $("[data-testid=description]");
    private final SelenideElement photo = $("[data-testid=paintingPhoto]");
    private final SelenideElement editButton = $("[data-testid=edit-painting]");

    public PaintingDetailPage(String paintingId) {
        this.paintingId = paintingId;
    }

    public PaintingDetailPage() {
        paintingId = "";
    }

    protected String getPageUrl() {
        return PAGE_URL + "/" + paintingId;
    }

    @Step("Open painting detail page")
    public PaintingDetailPage openPage() {
        Selenide.open(getPageUrl());
        return this;
    }

    @Override
    @Step("Wait for painting detail page is loaded")
    public PaintingDetailPage waitForPageIsLoaded() {
        super.waitForPageIsLoaded();
        title.shouldBe(visible);
        artist.shouldBe(visible);
        description.shouldBe(visible);
        photo.shouldBe(visible);
        return this;
    }

    @Step("Check painting title is: {title}")
    public PaintingDetailPage checkTitle(String title) {
        this.title.shouldHave(text(title));
        return this;
    }

    @Step("Check painting artist is: {artist}")
    public PaintingDetailPage checkArtist(String artist) {
        this.artist.shouldHave(text(artist));
        return this;
    }


    @Step("Check painting description is: {description}")
    public PaintingDetailPage checkDescription(String description) {
        this.description.shouldHave(text(description));
        return this;
    }

    @Step("Check painting photo")
    public PaintingDetailPage checkPhoto(String photo) {
        this.photo.shouldHave(attribute("src", photo));
        return this;
    }


    @Step("Edit painting")
    public PaintingModal editPainting() {
        editButton.click();
        return new PaintingModal();
    }

}
