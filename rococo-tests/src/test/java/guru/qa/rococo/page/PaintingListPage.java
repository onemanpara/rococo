package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.ItemsComponent;
import guru.qa.rococo.page.component.SearchComponent;
import guru.qa.rococo.page.component.modal.PaintingModal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class PaintingListPage extends BasePage<PaintingListPage> {

    private static final String PAGE_URL = CFG.rococoFrontendUrl() + "/painting";
    private final SelenideElement addNewItemButton = $("[data-testid=addNewItemButton]");
    private final SearchComponent searchComponent = new SearchComponent();
    private final ItemsComponent itemsComponent = new ItemsComponent();

    protected String getPageUrl() {
        return PAGE_URL;
    }

    @Step("Open painting list page")
    public PaintingListPage openPage() {
        Selenide.open(getPageUrl());
        return this;
    }

    @Override
    @Step("Wait for painting list page is loaded")
    public PaintingListPage waitForPageIsLoaded() {
        super.waitForPageIsLoaded();
        searchComponent.getSelf().shouldBe(visible);
        return this;
    }

    @Step("Add new painting")
    public PaintingModal addNewPainting() {
        addNewItemButton.click();
        return new PaintingModal();
    }

    @Step("Check painting list contains painting with name: {paintingName}")
    public PaintingListPage checkPaintingIsExist(String paintingName) {
        itemsComponent.getItemCard(paintingName).shouldBe(visible);
        return this;
    }

    @Step("Filter painting by title: {title}")
    public PaintingListPage filterPaintingByTitle(String title) {
        searchComponent.filterItemsByTitle(title);
        return this;
    }

    @Step("Check that empty painting search results contains text: {text}")
    public PaintingListPage checkEmptySearch(String text) {
        searchComponent.checkEmptySearchText(text);
        return this;
    }

    @Step("Check that painting list size is: {size}")
    public PaintingListPage checkPainitngListSize(int size) {
        itemsComponent.getItems().shouldHave(size(size));
        return this;
    }
}
