package guru.qa.rococo.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.ItemsComponent;
import guru.qa.rococo.page.component.SearchComponent;
import guru.qa.rococo.page.component.modal.ArtistModal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ArtistListPage extends BasePage<ArtistListPage> {

    private static final String PAGE_URL = CFG.rococoFrontendUrl() + "/artist";
    private final SelenideElement addNewItemButton = $("[data-testid=addNewItemButton]");
    private final SearchComponent searchComponent = new SearchComponent();
    private final ItemsComponent itemsComponent = new ItemsComponent();

    @Override
    protected String getPageUrl() {
        return PAGE_URL;
    }

    @Step("Open artist list page")
    public ArtistListPage openPage() {
        Selenide.open(getPageUrl());
        return this;
    }

    @Override
    @Step("Wait for artist list page is loaded")
    public ArtistListPage waitForPageIsLoaded() {
        super.waitForPageIsLoaded();
        searchComponent.getSelf().shouldBe(visible);
        return this;
    }

    @Step("Open artist card with title: {title}")
    public ArtistDetailPage openArtistCard(String title) {
        itemsComponent.getItemCard(title).click();
        return new ArtistDetailPage();
    }

    @Step("Filter artist by name: {name}")
    public ArtistListPage filterArtistByName(String name) {
        searchComponent.filterItemsByTitle(name);
        return this;
    }


    @Step("Add new artist")
    public ArtistModal addNewArtist() {
        addNewItemButton.click();
        return new ArtistModal();
    }

    @Step("Check artist list contains artist with name: {artistName}")
    public ArtistListPage checkArtistIsExist(String artistName) {
        itemsComponent.getItemCard(artistName).shouldBe(visible);
        return this;
    }

    @Step("Check that artist list size is: {size}")
    public ArtistListPage checkArtistListSize(int size) {
        itemsComponent.getItems().shouldHave(size(size));
        return this;
    }

    @Step("Check that empty artist search results contains text: {text}")
    public ArtistListPage checkEmptySearch(String text) {
        searchComponent.checkEmptySearchText(text);
        return this;
    }

}
