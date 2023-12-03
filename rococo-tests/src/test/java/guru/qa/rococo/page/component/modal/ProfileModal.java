package guru.qa.rococo.page.component.modal;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Input;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class ProfileModal extends BaseModal<ProfileModal> {

    private final SelenideElement username = $("[data-testid=username]");
    private final Input firstNameInput = new Input($("[name=firstname]"));
    private final Input surnameInput = new Input($("[name=surname]"));
    private final SelenideElement avatarInput = $("[name=content]");
    private final SelenideElement avatar = $("[data-testid=avatar]");
    private final SelenideElement logoutButton = $("[data-testid=logout]");

    @Step("Set first name: {firstName}")
    public ProfileModal setFirstName(String firstName) {
        firstNameInput.getSelf().setValue(firstName);
        return this;
    }

    @Step("Set surname: {surname}")
    public ProfileModal setSurname(String surname) {
        surnameInput.getSelf().setValue(surname);
        return this;
    }

    @Step("Set avatar")
    public ProfileModal setAvatar(String filepath) {
        avatarInput.uploadFromClasspath(filepath);
        return this;
    }

    @Step("Logout")
    public void logout() {
        this.logoutButton.click();
    }

    @Step("Check username is: {username}")
    public ProfileModal checkUsername(String username) {
        this.username.shouldHave(Condition.text("@" + username));
        return this;
    }

    @Step("Check first name is: {firstName}")
    public ProfileModal checkFirstname(String firstName) {
        firstNameInput.getSelf().shouldHave(value(firstName));
        return this;
    }

    @Step("Check surname is: {surname}")
    public ProfileModal checkSurname(String surname) {
        surnameInput.getSelf().shouldHave(value(surname));
        return this;
    }

    @Step("Check that user has custom avatar at profile modal")
    public ProfileModal checkPhoto(String photoBase64) {
        avatar.$("svg").shouldNot(exist);
        avatar.$("img").shouldBe(visible).shouldHave(attribute("src", photoBase64));
        return this;
    }

    @Step("Check first name error is: {error}")
    public ProfileModal checkFirstnameError(String error) {
        firstNameInput.checkError(error);
        return this;
    }

    @Step("Check surname error is: {error}")
    public ProfileModal checkSurnameError(String error) {
        surnameInput.checkError(error);
        return this;
    }

}