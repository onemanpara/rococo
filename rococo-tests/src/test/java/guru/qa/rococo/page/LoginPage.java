package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Input;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage<LoginPage> {

    private static final String PAGE_URL = CFG.rococoAuthUrl() + "/login";
    private final Input usernameInput = new Input($("input[name=username]"));
    private final Input passwordInput = new Input($("input[name=password]"));
    private final SelenideElement submitButton = $("button.form__submit");
    private final SelenideElement registerLink = $("[data-testid=registerLink]");
    private final SelenideElement error = $(".form__error");

    protected String getPageUrl() {
        return PAGE_URL;
    }

    @Override
    @Step("Wait for login page is loaded")
    public LoginPage waitForPageIsLoaded() {
        super.waitForPageIsLoaded();
        usernameInput.getSelf().shouldBe(visible);
        passwordInput.getSelf().shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

    @Step("Fill login page with credentials: username: {username}, password: {password}")
    public LoginPage fillLoginPage(String username, String password) {
        setUsername(username);
        setPassword(password);
        return this;
    }

    @Step("Set username: {username}")
    public LoginPage setUsername(String username) {
        usernameInput.getSelf().setValue(username);
        return this;
    }

    @Step("Set password: {password}")
    public LoginPage setPassword(String password) {
        passwordInput.getSelf().setValue(password);
        return this;
    }

    @Step("Success submit login form")
    public MainPage successSubmit() {
        submitButton.click();
        return new MainPage();
    }

    @Step("Submit login form with error")
    public LoginPage errorSubmit() {
        submitButton.click();
        return this;
    }

    @Step("Go to registration page")
    public RegistrationPage register() {
        registerLink.click();
        return new RegistrationPage();
    }

    @Step("Check error is: {error}")
    public LoginPage checkError(String error) {
        this.error.shouldBe(visible).shouldHave(text(error));
        return this;
    }

}
