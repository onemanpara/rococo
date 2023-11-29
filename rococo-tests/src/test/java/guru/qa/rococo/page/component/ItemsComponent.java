package guru.qa.rococo.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.util.NoSuchElementException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class ItemsComponent {

    private final ElementsCollection items = $$("[data-testid=item]");

    public SelenideElement getItemCard(String itemName) {
        int initialItemsCount = items.size();
        SelenideElement requireItem = items.find(text(itemName));

        while (!requireItem.exists()) {
            items.last().scrollIntoView(true);
            waitForNewItemsAreLoaded(initialItemsCount, itemName);
            initialItemsCount = items.size();
            requireItem = items.find(text(itemName));
        }

        return requireItem;
    }

    public ElementsCollection getItems() {
        return items;
    }

    private void waitForNewItemsAreLoaded(int initialItemsCount, String itemName) {
        try {
            Selenide.Wait().until(webDriver -> items.size() > initialItemsCount);
        } catch (Exception e) {
            throw new NoSuchElementException("Item with text: '" + itemName + "' not found. " +
                    "Timed out waiting for new items to be loaded. Current items count: " + items.size(), e);
        }
    }
}
