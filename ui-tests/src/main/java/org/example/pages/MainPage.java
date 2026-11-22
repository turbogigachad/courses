package org.example.pages;

import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

// Главная страница магазина
public class MainPage {

    private final SelenideElement mainTitle = $("#main-title");
    private final SelenideElement adminLink = $("a.btn-outline[href='/admin']");
    private final ElementsCollection productCards = $$(".product-card");
    private final ElementsCollection quantityInputs = $$(".product-card .qty-input");
    private final ElementsCollection addToCartButtons = $$(".product-card button[data-action='add-to-cart']");
    private final SelenideElement openCartButton = $("#open-cart-btn");
    private final SelenideElement cartModal = $("#cartModal");
    private final SelenideElement closeCartButton = $("#close-modal");
    private final ElementsCollection cartItems = $$("#cart-items .cart-item");
    private final SelenideElement emptyCartMessage = $("#empty-cart");
    private final SelenideElement cartCount = $("#cart-count");
    private final SelenideElement totalPrice = $("#total-price");
    private final SelenideElement makeOrderButton = $("#makeOrder");
    private final SelenideElement toastContainer = $("#toast-container");

    @Step("Открыть главную страницу магазина")
    public MainPage openPage() {
        open("/");
        return shouldBeOpened();
    }

    @Step("Проверить видимость основных элементов витрины")
    public MainPage shouldBeOpened() {
        mainTitle.shouldBe(visible);
        adminLink.shouldBe(visible);
        openCartButton.shouldBe(visible);
        productCards.first().shouldBe(visible);
        quantityInputs.first().shouldBe(visible).shouldHave(value("1"));
        addToCartButtons.first().shouldBe(visible);
        return this;
    }

    @Step("Перейти в администрирование")
    public MainPage clickAdministration() {
        adminLink.shouldBe(visible).click();
        return this;
    }

    @Step("Установить товару с индексом {productIndex} количество {quantity}")
    public MainPage setProductQuantity(int productIndex, int quantity) {
        quantityInputs.get(productIndex)
                .shouldBe(visible)
                .setValue(String.valueOf(quantity))
                .shouldHave(value(String.valueOf(quantity)));
        return this;
    }

    @Step("Установить товару '{productName}' количество {quantity}")
    public MainPage setProductQuantity(String productName, int quantity) {
        getProductCard(productName).$(".qty-input")
                .shouldBe(visible)
                .setValue(String.valueOf(quantity))
                .shouldHave(value(String.valueOf(quantity)));
        return this;
    }

    @Step("Добавить в корзину товар с индексом {productIndex}")
    public MainPage addProductToCart(int productIndex) {
        productCards.get(productIndex).shouldBe(visible);
        addToCartButtons.get(productIndex).shouldBe(visible).click();
        return this;
    }

    @Step("Добавить в корзину {quantity} шт. товара '{productName}'")
    public MainPage addProductToCart(String productName, int quantity) {
        setProductQuantity(productName, quantity);
        getProductCard(productName).$("button[data-action='add-to-cart']")
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Добавить первый товар в корзину")
    public String addFirstProductToCart() {
        SelenideElement firstCard = productCards.first().shouldBe(visible);
        String productName = firstCard.attr("data-name");
        firstCard.$("button[data-action='add-to-cart']").shouldBe(visible).click();
        return productName;
    }

    @Step("Перетащить первый товар в корзину")
    public String dragFirstProductToCart() {
        SelenideElement firstCard = productCards.first().shouldBe(visible);
        String productName = firstCard.attr("data-name");
        firstCard.dragAndDrop(DragAndDropOptions.to(openCartButton));
        return productName;
    }

    @Step("Открыть корзину")
    public MainPage openCart() {
        openCartButton.shouldBe(visible).click();
        cartModal.shouldBe(visible);
        closeCartButton.shouldBe(visible);
        totalPrice.shouldBe(visible);
        makeOrderButton.shouldBe(visible);
        return this;
    }

    @Step("Закрыть корзину")
    public MainPage closeCart() {
        closeCartButton.shouldBe(visible).click();
        cartModal.shouldBe(hidden);
        return this;
    }

    @Step("Удалить из корзины товар с индексом {itemIndex}")
    public MainPage removeCartItem(int itemIndex) {
        cartItems.get(itemIndex)
                .shouldBe(visible)
                .$("button[data-action='remove']")
                .shouldBe(visible)
                .click();
        return this;
    }

    @Step("Нажать «Оформить заказ»")
    public MainPage makeOrder() {
        makeOrderButton.shouldBe(visible).click();
        return this;
    }

    @Step("Обновить страницу магазина")
    public MainPage refreshPage() {
        refresh();
        return shouldBeOpened();
    }

    @Step("Проверить, что товар '{productName}' отображается на витрине")
    public MainPage shouldHaveProduct(String productName) {
        productCards.filterBy(text(productName)).first().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что товар '{productName}' отображается в корзине")
    public MainPage shouldHaveCartItem(String productName) {
        cartItems.filterBy(text(productName)).first().shouldBe(visible);
        return this;
    }

    @Step("Проверить общую стоимость корзины: {expectedPrice}")
    public MainPage shouldHaveTotalPrice(String expectedPrice) {
        totalPrice.shouldBe(visible).shouldHave(exactText(expectedPrice));
        return this;
    }

    public SelenideElement getProductCard(String productName) {
        return productCards.filterBy(attribute("data-name", productName)).first();
    }

    @Step("Проверить наличие карточек товаров")
    public ElementsCollection getProductCards() {
        productCards.first().shouldBe(visible);
        return productCards;
    }

    public ElementsCollection getCartItems() {
        return cartItems;
    }

    public SelenideElement getMainTitle() {
        return mainTitle;
    }

    public SelenideElement getEmptyCartMessage() {
        return emptyCartMessage;
    }

    public SelenideElement getCartCount() {
        return cartCount;
    }

    public SelenideElement getTotalPrice() {
        return totalPrice;
    }

    public SelenideElement getToastContainer() {
        return toastContainer;
    }
}
