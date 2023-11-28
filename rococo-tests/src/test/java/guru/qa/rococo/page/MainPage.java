package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;

public class MainPage extends BasePage<MainPage> {

    private final SelenideElement paintingLink = Selenide.$("[data-testid=painting]");
    private final SelenideElement artistLink = Selenide.$("[data-testid=artist]");
    private final SelenideElement museumLink = Selenide.$("[data-testid=museum]");

    protected String getPageUrl() {
        return CFG.rococoFrontendUrl();
    }

    @Step("Open main page")
    public MainPage openPage() {
        Selenide.open(getPageUrl());
        return this;
    }

    @Step("Wait for main page is loaded")
    public MainPage waitForPageIsLoaded() {
        super.waitForPageIsLoaded();
        paintingLink.shouldBe(visible);
        artistLink.shouldBe(visible);
        museumLink.shouldBe(visible);
        return this;
    }

}

