package org.example.pages.asserts;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.example.pages.MainPage;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.confirm;

// Проверки состояния главной страницы и корзины
public class MainPageAssert {

    private final MainPage mainPage;

    public MainPageAssert(MainPage mainPage) {
        this.mainPage = mainPage;
    }

    @Step("Проверить количество позиций в корзине: {expectedCount}")
    public MainPageAssert shouldHaveCartItemsCount(int expectedCount) {
        mainPage.getCartItems().shouldHave(size(expectedCount));
        return this;
    }

    @Step("Проверить товар '{productName}' в корзине")
    public MainPageAssert shouldHaveCartItem(String productName) {
        mainPage.getCartItems().filterBy(text(productName)).first().shouldBe(visible);
        return this;
    }

    @Step("Проверить общую стоимость корзины: {expectedPrice}")
    public MainPageAssert shouldHaveTotalPrice(int expectedPrice) {
        mainPage.getTotalPrice()
                .shouldBe(visible)
                .shouldHave(exactText(String.valueOf(expectedPrice)));
        return this;
    }

    @Step("Проверить количество единиц товаров в корзине: {expectedCount}")
    public MainPageAssert shouldHaveCartCount(int expectedCount) {
        mainPage.getCartCount()
                .shouldBe(visible)
                .shouldHave(exactText(String.valueOf(expectedCount)));
        return this;
    }

    @Step("Проверить уведомление об обработке заказа")
    public MainPageAssert shouldHaveOrderProcessedNotification() {
        mainPage.getMainTitle().shouldHave(text("Заказ успешно оформлен!"));
        mainPage.getToastContainer().$$(".toast")
                .filterBy(text("Заказ принят в обработку!"))
                .first()
                .shouldBe(visible);
        return this;
    }

    @Step("Проверить Alert о превышении лимита заказа для суммы {expectedTotal}")
    public MainPageAssert shouldHaveOrderOverLimitAlert(int expectedTotal) {
        confirm("[SmartShop]: Денег не хватает! Сумма " + expectedTotal + " ₽ превышает лимит 300 ₽.");
        return this;
    }

    @Step("Проверить пустую корзину")
    public MainPageAssert shouldHaveEmptyCart() {
        mainPage.getCartItems().shouldHave(size(0));
        mainPage.getEmptyCartMessage().shouldBe(visible).shouldHave(exactText("Пусто"));
        shouldHaveCartCount(0);
        return this;
    }

    @Step("Проверить на витрине товар '{expectedName}' с ценой '{expectedPrice}'")
    public MainPageAssert shouldHaveProduct(String expectedName, String expectedPrice) {
        SelenideElement productCard = mainPage.getProductCard(expectedName).shouldBe(visible);
        productCard.$("h4").shouldHave(exactText(expectedName));
        productCard.$("h4 + div").shouldHave(exactText(expectedPrice));
        return this;
    }
}
