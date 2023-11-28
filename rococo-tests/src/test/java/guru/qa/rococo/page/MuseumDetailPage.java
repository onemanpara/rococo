package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.modal.MuseumModal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;

public class MuseumDetailPage extends BasePage<MuseumDetailPage> {
    private static final String PAGE_URL = CFG.rococoFrontendUrl() + "/museum";
    private final String museumId;
    private final SelenideElement title = Selenide.$(".card-header");
    private final SelenideElement geo = Selenide.$("[data-testid=geo]");
    private final SelenideElement description = Selenide.$("[data-testid=description]");
    private final SelenideElement photo = Selenide.$("[data-testid=museumPhoto]");
    private final SelenideElement editButton = Selenide.$("[data-testid=edit-museum]");

    public MuseumDetailPage(String museumId) {
        this.museumId = museumId;
    }

    public MuseumDetailPage() {
        this.museumId = "";
    }

    protected String getPageUrl() {
        return PAGE_URL + "/" + this.museumId;
    }

    @Step("Open museum detail page")
    public MuseumDetailPage openPage() {
        Selenide.open(getPageUrl());
        return this;
    }

    @Override
    @Step("Wait for museum detail page is loaded")
    public MuseumDetailPage waitForPageIsLoaded() {
        super.waitForPageIsLoaded();
        title.shouldBe(visible);
        geo.shouldBe(visible);
        description.shouldBe(visible);
        photo.shouldBe(visible);
        return this;
    }

    @Step("Check museum title is: {title}")
    public MuseumDetailPage checkTitle(String title) {
        this.title.shouldHave(text(title));
        return this;
    }

    @Step("Check museum geo is: {country}, {city}")
    public MuseumDetailPage checkGeo(String country, String city) {
        geo.shouldHave(text(country + ", " + city));
        return this;
    }

    @Step("Check museum description is: {description}")
    public MuseumDetailPage checkDescription(String description) {
        this.description.shouldHave(text(description));
        return this;
    }

    @Step("Check museum photo")
    public MuseumDetailPage checkPhoto(String photo) {
        this.photo.shouldHave(attribute("src", photo));
        return this;
    }


    @Step("Edit museum")
    public MuseumModal editMuseum() {
        editButton.click();
        return new MuseumModal();
    }

}
