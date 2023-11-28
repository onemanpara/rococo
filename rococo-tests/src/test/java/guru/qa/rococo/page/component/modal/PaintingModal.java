package guru.qa.rococo.page.component.modal;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Input;
import guru.qa.rococo.page.component.Select;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class PaintingModal extends BaseModal<PaintingModal> {

    private final Input titleInput = new Input($("[name=title]"));
    private final SelenideElement photoInput = $("[name=content]");
    private final Select artistSelect = new Select($("[name=authorId]"));
    private final Input descriptionInput = new Input($("[name=description]"));
    private final Select museumSelect = new Select($("[name=museumId]"));

    @Step("Set painting title: {title}")
    public PaintingModal setTitle(String title) {
        titleInput.getSelf().setValue(title);
        return this;
    }

    @Step("Set painting photo")
    public PaintingModal setPhoto(String filepath) {
        photoInput.uploadFromClasspath(filepath);
        return this;
    }

    @Step("Select painting's artist: {artist}")
    public PaintingModal selectArtist(String artist) {
        artistSelect.selectOption(artist);
        return this;
    }

    @Step("Set painting description: {description}")
    public PaintingModal setDescription(String description) {
        descriptionInput.getSelf().setValue(description);
        return this;
    }

    @Step("Select painting's museum: {museum}")
    public PaintingModal selectMuseum(String museum) {
        museumSelect.selectOption(museum);
        return this;
    }

    @Step("Check title error is: {error}")
    public PaintingModal checkTitleError(String error) {
        titleInput.checkError(error);
        return this;
    }

    @Step("Check description error is: {error}")
    public PaintingModal checkDescriptionError(String error) {
        descriptionInput.checkError(error);
        return this;
    }

}
