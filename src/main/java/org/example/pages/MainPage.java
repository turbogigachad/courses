package org.example.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.hidden;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

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
    private final SelenideElement cartCount = $("#cart-count");
    private final SelenideElement totalPrice = $("#total-price");
    private final SelenideElement makeOrderButton = $("#makeOrder");
    private final SelenideElement toastContainer = $("#toast-container");

    public MainPage openPage() {
        open("/");
        return shouldBeOpened();
    }

    public MainPage shouldBeOpened() {
        mainTitle.shouldBe(visible);
        adminLink.shouldBe(visible);
        openCartButton.shouldBe(visible);
        productCards.first().shouldBe(visible);
        quantityInputs.first().shouldBe(visible).shouldHave(value("1"));
        addToCartButtons.first().shouldBe(visible);
        return this;
    }

    public MainPage clickAdministration() {
        adminLink.shouldBe(visible).click();
        return this;
    }

    public MainPage setProductQuantity(int productIndex, int quantity) {
        quantityInputs.get(productIndex)
                .shouldBe(visible)
                .setValue(String.valueOf(quantity))
                .shouldHave(value(String.valueOf(quantity)));
        return this;
    }

    public MainPage setProductQuantity(String productName, int quantity) {
        getProductCard(productName).$(".qty-input")
                .shouldBe(visible)
                .setValue(String.valueOf(quantity))
                .shouldHave(value(String.valueOf(quantity)));
        return this;
    }

    public MainPage addProductToCart(int productIndex) {
        productCards.get(productIndex).shouldBe(visible);
        addToCartButtons.get(productIndex).shouldBe(visible).click();
        return this;
    }

    public MainPage addProductToCart(String productName, int quantity) {
        setProductQuantity(productName, quantity);
        getProductCard(productName).$("button[data-action='add-to-cart']")
                .shouldBe(visible)
                .click();
        return this;
    }

    public MainPage openCart() {
        openCartButton.shouldBe(visible).click();
        cartModal.shouldBe(visible);
        closeCartButton.shouldBe(visible);
        totalPrice.shouldBe(visible);
        makeOrderButton.shouldBe(visible);
        return this;
    }

    public MainPage closeCart() {
        closeCartButton.shouldBe(visible).click();
        cartModal.shouldBe(hidden);
        return this;
    }

    public MainPage removeCartItem(int itemIndex) {
        cartItems.get(itemIndex)
                .shouldBe(visible)
                .$("button[data-action='remove']")
                .shouldBe(visible)
                .click();
        return this;
    }

    public MainPage makeOrder() {
        makeOrderButton.shouldBe(visible).click();
        return this;
    }

    public MainPage shouldHaveProduct(String productName) {
        productCards.filterBy(text(productName)).first().shouldBe(visible);
        return this;
    }

    public MainPage shouldHaveCartItem(String productName) {
        cartItems.filterBy(text(productName)).first().shouldBe(visible);
        return this;
    }

    public MainPage shouldHaveTotalPrice(String expectedPrice) {
        totalPrice.shouldBe(visible).shouldHave(exactText(expectedPrice));
        return this;
    }

    public SelenideElement getProductCard(String productName) {
        return productCards.filterBy(attribute("data-name", productName)).first();
    }

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
