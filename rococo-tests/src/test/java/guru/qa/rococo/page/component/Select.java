package guru.qa.rococo.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import java.util.NoSuchElementException;

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

            options.last().scrollIntoView(true);
            waitForNewOptionsAreLoaded(initialOptionsCount, option);
            initialOptionsCount = options.size();
        }
    }

    private void waitForNewOptionsAreLoaded(int initialOptionsCount, String optionName) {
        try {
            Selenide.Wait().until(webDriver -> options.size() > initialOptionsCount);
        } catch (Exception e) {
            throw new NoSuchElementException("Option with text: '" + optionName + "' not found. " +
                    "Timed out waiting for new options to be loaded. Current options count: " + options.size(), e);
        }
    }

}