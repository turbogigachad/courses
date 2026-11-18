package org.example.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class AdminPage {

    private final SelenideElement newProductNameInput = $("#n-name");
    private final SelenideElement newProductPriceInput = $("#n-price");
    private final SelenideElement addProductButton = $("#add-btn");
    private final SelenideElement returnToStorefrontLink = $("a[href='/']");
    private final SelenideElement toastContainer = $("#toast-container");

    public AdminPage addProduct(String name, String price) {
        newProductNameInput.shouldBe(visible)
                .setValue(name)
                .shouldHave(value(name));
        newProductPriceInput.shouldBe(visible)
                .setValue(price)
                .shouldHave(value(price));
        addProductButton.shouldBe(visible).click();
        return this;
    }

    public AdminPage updateProduct(int id, String newName, String newPrice) {
        $("#nm-" + id).shouldBe(visible)
                .setValue(newName)
                .shouldHave(value(newName));
        $("#pr-" + id).shouldBe(visible)
                .setValue(newPrice)
                .shouldHave(value(newPrice));
        $("button[data-action='update'][data-id='" + id + "']")
                .shouldBe(visible)
                .click();
        return this;
    }

    public MainPage returnToStorefront() {
        returnToStorefrontLink.shouldBe(visible).click();
        return new MainPage();
    }

    public SelenideElement getToastContainer() {
        return toastContainer;
    }
}
