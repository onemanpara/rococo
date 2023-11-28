package guru.qa.rococo.page.component.modal;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.BaseComponent;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public abstract class BaseModal<T extends BaseComponent> extends BaseComponent<T> {

    private final SelenideElement submitFormButton = $("button[type=submit]");

    public BaseModal() {
        super($("[data-testid=modal-component]"));
    }

    @Step("Success submit form")
    public void successSubmit() {
        submitFormButton.click();
        self.shouldNotBe(visible);
    }

    @Step("Submit form with error")
    @SuppressWarnings("unchecked")
    public T errorSubmit() {
        submitFormButton.click();
        return (T) this;
    }

}