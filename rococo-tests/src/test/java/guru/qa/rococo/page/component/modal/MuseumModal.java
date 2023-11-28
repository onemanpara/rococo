package guru.qa.rococo.page.component.modal;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Input;
import guru.qa.rococo.page.component.Select;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class MuseumModal extends BaseModal<MuseumModal> {

    private final Input titleInput = new Input($("[name=title]"));
    private final Select countrySelect = new Select($("[name=countryId]"));
    private final Input cityInput = new Input($("[name=city]"));
    private final SelenideElement photoInput = $("[name=photo]");
    private final Input descriptionInput = new Input($("[name=description]"));

    @Step("Set museum title: {title}")
    public MuseumModal setTitle(String title) {
        titleInput.getSelf().setValue(title);
        return this;
    }

    @Step("Select museum country: {country}")
    public MuseumModal selectCountry(String country) {
        countrySelect.selectOption(country);
        return this;
    }

    @Step("Set museum city: {city}")
    public MuseumModal setCity(String city) {
        cityInput.getSelf().setValue(city);
        return this;
    }

    @Step("Set museum photo")
    public MuseumModal setPhoto(String filepath) {
        photoInput.uploadFromClasspath(filepath);
        return this;
    }

    @Step("Set museum description: {description}")
    public MuseumModal setDescription(String description) {
        descriptionInput.getSelf().setValue(description);
        return this;
    }

    @Step("Check title error is: {error}")
    public MuseumModal checkTitleError(String error) {
        titleInput.checkError(error);
        return this;
    }

    @Step("Check city error is: {error}")
    public MuseumModal checkCityError(String error) {
        cityInput.checkError(error);
        return this;
    }

    @Step("Check description error is: {error}")
    public MuseumModal checkDescriptionError(String error) {
        descriptionInput.checkError(error);
        return this;
    }

}