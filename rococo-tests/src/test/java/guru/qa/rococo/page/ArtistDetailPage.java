package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.ItemsComponent;
import guru.qa.rococo.page.component.modal.ArtistModal;
import guru.qa.rococo.page.component.modal.PaintingModal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class ArtistDetailPage extends BasePage<ArtistDetailPage> {
    private static final String PAGE_URL = CFG.rococoFrontendUrl() + "/artist";
    private final String artistId;
    private final SelenideElement name = $(".card-header");
    private final SelenideElement biography = $("[data-testid=biography]");
    private final SelenideElement photo = $("[data-testid=artistPhoto]");
    private final SelenideElement editButton = $("[data-testid=edit-artist]");
    private final ItemsComponent itemsComponent = new ItemsComponent();
    private final SelenideElement addPaintingButton = $("[data-testid=addPainting]");

    public ArtistDetailPage(String artistId) {
        this.artistId = artistId;
    }

    public ArtistDetailPage() {
        this.artistId = "";
    }

    protected String getPageUrl() {
        return PAGE_URL + "/" + this.artistId;
    }

    @Step("Open artist detail page")
    public ArtistDetailPage openPage() {
        Selenide.open(getPageUrl());
        return this;
    }

    @Override
    @Step("Wait for artist detail page is loaded")
    public ArtistDetailPage waitForPageIsLoaded() {
        super.waitForPageIsLoaded();
        name.shouldBe(visible);
        biography.shouldBe(visible);
        photo.shouldBe(visible);
        return this;
    }

    @Step("Add new painting")
    public PaintingModal addNewPainting() {
        addPaintingButton.click();
        return new PaintingModal();
    }

    @Step("Check artist name is: {name}")
    public ArtistDetailPage checkName(String name) {
        this.name.shouldHave(text(name));
        return this;
    }

    @Step("Check artist biography is: {biography}")
    public ArtistDetailPage checkBiography(String biography) {
        this.biography.shouldHave(text(biography));
        return this;
    }

    @Step("Check museum photo")
    public ArtistDetailPage checkPhoto(String photo) {
        this.photo.shouldHave(attribute("src", photo));
        return this;
    }

    @Step("Open painting card with title: {title}")
    public PaintingDetailPage openPaintingCard(String title) {
        itemsComponent.getItemCard(title).click();
        return new PaintingDetailPage();
    }

    @Step("Edit artist")
    public ArtistModal editArtist() {
        editButton.click();
        return new ArtistModal();
    }

}