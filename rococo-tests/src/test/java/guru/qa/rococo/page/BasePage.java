package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverConditions;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public abstract class BasePage<T extends BasePage> {

    protected static final Config CFG = Config.getConfig();
    private final SelenideElement notification = Selenide.$("[data-testid=toast]");

    protected abstract String getPageUrl();

    @SuppressWarnings("unchecked")
    public T waitForPageIsLoaded() {
        Selenide.webdriver().shouldHave(WebDriverConditions.urlContaining(this.getPageUrl()));
        return (T) this;
    }

    @Step("Getting header")
    public Header getHeader() {
        return new Header();
    }

    @Step("Check notification text is: {text}")
    @SuppressWarnings("unchecked")
    public T checkNotificationText(String text) {
        this.notification.shouldBe(visible).shouldHave(text(text));
        return (T) this;
    }

}