package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.LoginPage;
import guru.qa.rococo.page.component.modal.ProfileModal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    private final SelenideElement loginButton = $("[data-testid=loginButton]");
    private final SelenideElement avatar = $("[data-testid=avatar]");

    public Header() {
        super($("#shell-header"));
    }

    @Step("Click login button")
    public LoginPage login() {
        loginButton.click();
        return new LoginPage();
    }

    @Step("Open profile modal")
    public ProfileModal openProfileModal() {
        avatar.click();
        return new ProfileModal();
    }

    @Step("Check that user is authorized")
    public Header checkUserIsAuthorized() {
        loginButton.shouldNotBe(visible);
        avatar.shouldBe(visible);
        return this;
    }

    @Step("Check that user is not authorized")
    public Header checkUserIsNotAuthorized() {
        loginButton.shouldBe(visible);
        avatar.shouldNotBe(visible);
        return this;
    }

    @Step("Check that user has custom avatar at header")
    public Header checkPhoto(String photoBase64) {
        avatar.$("svg").shouldNot(exist);
        avatar.$("img").shouldBe(visible).shouldHave(attribute("src", photoBase64));
        return this;
    }

}
