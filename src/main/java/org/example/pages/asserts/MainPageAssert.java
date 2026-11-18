package org.example.pages.asserts;

import com.codeborne.selenide.SelenideElement;
import org.example.pages.MainPage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class MainPageAssert {

    private final MainPage mainPage;

    public MainPageAssert(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    public MainPageAssert shouldHaveCartItemsCount(int expectedCount) {
        mainPage.getCartItems().shouldHave(size(expectedCount));
        return this;
    }

    public MainPageAssert shouldHaveTotalPrice(int expectedPrice) {
        mainPage.getTotalPrice()
                .shouldBe(visible)
                .shouldHave(exactText(String.valueOf(expectedPrice)));
        return this;
    }

    public MainPageAssert shouldHaveCartCount(int expectedCount) {
        mainPage.getCartCount()
                .shouldBe(visible)
                .shouldHave(exactText(String.valueOf(expectedCount)));
        return this;
    }

    public MainPageAssert shouldHaveOrderProcessedNotification() {
        mainPage.getMainTitle().shouldHave(text("Заказ успешно оформлен!"));
        mainPage.getToastContainer().$$(".toast")
                .filterBy(text("Заказ принят в обработку!"))
                .first()
                .shouldBe(visible);
        return this;
    }

    public MainPageAssert shouldHaveProduct(String expectedName, String expectedPrice) {
        SelenideElement productCard = mainPage.getProductCard(expectedName).shouldBe(visible);
        productCard.$("h4").shouldHave(exactText(expectedName));
        productCard.$("h4 + div").shouldHave(exactText(expectedPrice));
        return this;
    }
}
