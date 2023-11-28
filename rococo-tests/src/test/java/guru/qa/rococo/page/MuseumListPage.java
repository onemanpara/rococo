package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.ItemsComponent;
import guru.qa.rococo.page.component.SearchComponent;
import guru.qa.rococo.page.component.modal.MuseumModal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MuseumListPage extends BasePage<MuseumListPage> {

    private static final String PAGE_URL = CFG.rococoFrontendUrl() + "/museum";
    private final SelenideElement addNewItemButton = $("[data-testid=addNewItemButton]");
    private final SearchComponent searchComponent = new SearchComponent();
    private final ItemsComponent itemsComponent = new ItemsComponent();

    @Override
    protected String getPageUrl() {
        return PAGE_URL;
    }

    @Step("Open museum list page")
    public MuseumListPage openPage() {
        Selenide.open(getPageUrl());
        return this;
    }

    @Step("Wait for museum list page is loaded")
    public MuseumListPage waitForPageIsLoaded() {
        super.waitForPageIsLoaded();
        searchComponent.getSelf().shouldBe(visible);
        return this;
    }

    @Step("Open museum card with title: {title}")
    public MuseumDetailPage openMuseumCard(String title) {
        itemsComponent.getItemCard(title).click();
        return new MuseumDetailPage();
    }

    @Step("Filter museums by title: {title}")
    public MuseumListPage filterMuseumsByTitle(String title) {
        searchComponent.filterItemsByTitle(title);
        return this;
    }

    @Step("Add new museum")
    public MuseumModal addNewMuseum() {
        addNewItemButton.click();
        return new MuseumModal();
    }

    @Step("Check that museum list contains museum with name: {museumName}, city: {museumCity}, country: {museumCountry}")
    public MuseumListPage checkMuseumIsExist(String museumName, String museumCity, String museumCountry) {
        itemsComponent.getItemCard(museumName).shouldBe(visible).shouldHave(text(museumCity + ", " + museumCountry));
        return this;
    }

    @Step("Check that museum list size is: {size}")
    public MuseumListPage checkMuseumListSize(int size) {
        itemsComponent.getItems().shouldHave(size(size));
        return this;
    }

    @Step("Check that empty museum search results contains text: {text}")
    public MuseumListPage checkEmptySearch(String text) {
        searchComponent.checkEmptySearchText(text);
        return this;
    }
}