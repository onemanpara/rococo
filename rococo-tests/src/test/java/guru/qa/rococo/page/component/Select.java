package guru.qa.rococo.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;

public class Select extends BaseComponent<Select> {

    private final ElementsCollection options = self.$$("option");

    public Select(@Nonnull SelenideElement self) {
        super(self);
    }

    public void selectOption(String option) {
        int initialOptionsCount = options.size();

        while (true) {
            SelenideElement requiredOption = options.find(text(option));
            if (requiredOption.exists()) {
                requiredOption.click();
                return;
            }

            options.last().scrollIntoView(true).click();
            options.shouldHave(sizeGreaterThan(initialOptionsCount)
                    .because("Option with text: '" + option + "' not found. " +
                            "Timed out waiting for new options to be loaded. Current options count:" + options.size()));
            initialOptionsCount = options.size();
        }
    }

}