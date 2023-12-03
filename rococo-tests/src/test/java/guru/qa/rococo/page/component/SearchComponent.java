package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class SearchComponent extends BaseComponent<SearchComponent> {

    public SearchComponent() {
        super($("[data-testid=searchComponent]"));
    }

    private final SelenideElement searchInput = $("[data-testid=searchInput]");
    private final SelenideElement searchButton = $("[data-testid=searchButton]");
    private final SelenideElement emptySearch = $("[data-testid=emptySearch]");

    public void filterItemsByTitle(String title) {
        searchInput.setValue(title);
        searchButton.click();
    }

    public void checkEmptySearchText(String text) {
        emptySearch.shouldBe(visible).shouldHave(text(text));
    }

}
