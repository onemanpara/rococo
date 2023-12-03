package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Input;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage extends BasePage<RegistrationPage> {

    private static final String PAGE_URL = CFG.rococoAuthUrl() + "/register";
    private final Input usernameInput = new Input($("[id=username]"));
    private final Input passwordInput = new Input($("[id=password]"));
    private final Input confirmPasswordInput = new Input($("[id=passwordSubmit]"));
    private final SelenideElement submitButton = $("button.form__submit");
    private final SelenideElement loginButton = $("[data-testid=loginButton]");

    protected String getPageUrl() {
        return PAGE_URL;
    }

    @Override
    @Step("Wait for registration page is loaded")
    public RegistrationPage waitForPageIsLoaded() {
        super.waitForPageIsLoaded();
        usernameInput.getSelf().shouldBe(visible);
        passwordInput.getSelf().shouldBe(visible);
        confirmPasswordInput.getSelf().shouldBe(visible);
        submitButton.shouldBe(visible);
        return this;
    }

    @Step("Fill registration form with credentials: username: {username}, password: {password}, confirmPassword: {confirmPassword}")
    public RegistrationPage fillRegistrationPage(String username, String password, String confirmPassword) {
        setUsername(username);
        setPassword(password);
        setConfirmPassword(confirmPassword);
        return this;
    }

    @Step("Set username: {username}")
    public RegistrationPage setUsername(String username) {
        usernameInput.getSelf().setValue(username);
        return this;
    }

    @Step("Set password: {password}")
    public RegistrationPage setPassword(String password) {
        passwordInput.getSelf().setValue(password);
        return this;
    }

    @Step("Set confirm password: {confirmPassword}")
    public RegistrationPage setConfirmPassword(String confirmPassword) {
        confirmPasswordInput.getSelf().setValue(confirmPassword);
        return this;
    }

    @Step("Success submit registration form")
    public RegistrationPage successSubmit() {
        submitButton.click();
        return this;
    }

    @Step("Submit registration form with error")
    public RegistrationPage errorSubmit() {
        submitButton.click();
        return this;
    }

    @Step("Go to login page")
    public LoginPage login() {
        loginButton.click();
        return new LoginPage();
    }

    @Step("Check username error is: {error}")
    public RegistrationPage checkUsernameError(String error) {
        usernameInput.checkError(error);
        return this;
    }

    @Step("Check password error is: {error}")
    public RegistrationPage checkPasswordError(String error) {
        passwordInput.checkError(error);
        return this;
    }

}

