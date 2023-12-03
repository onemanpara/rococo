package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class Input extends BaseComponent<Input> implements Validatable {

    public Input(@Nonnull SelenideElement self) {
        super(self);
    }

    public void checkError(String text) {
        self.sibling(0).shouldBe(visible).shouldHave(text(text));
    }

}
