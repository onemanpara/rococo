package guru.qa.rococo.page.component.modal;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Input;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class ArtistModal extends BaseModal<ArtistModal> {

    private final Input nameInput = new Input($("[name=name]"));
    private final SelenideElement photoInput = $("[name=photo]");
    private final Input biographyInput = new Input($("[name=biography]"));

    @Step("Set artist name: {name}")
    public ArtistModal setName(String name) {
        nameInput.getSelf().setValue(name);
        return this;
    }

    @Step("Set artist photo")
    public ArtistModal setPhoto(String filepath) {
        photoInput.uploadFromClasspath(filepath);
        return this;
    }

    @Step("Set artist biography: {biography}")
    public ArtistModal setBiography(String biography) {
        biographyInput.getSelf().setValue(biography);
        return this;
    }

    @Step("Check name error is: {error}")
    public ArtistModal checkNameError(String error) {
        nameInput.checkError(error);
        return this;
    }

    @Step("Check biography error is: {error}")
    public ArtistModal checkBiographyError(String error) {
        biographyInput.checkError(error);
        return this;
    }

}