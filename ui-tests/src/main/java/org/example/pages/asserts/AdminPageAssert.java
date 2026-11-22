package org.example.pages.asserts;

import io.qameta.allure.Step;
import org.example.pages.AdminPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;

public class AdminPageAssert {

    private final AdminPage adminPage;

    public AdminPageAssert(AdminPage adminPage) {
        this.adminPage = adminPage;
    }

    @Step("Проверить уведомление об успешном добавлении товара")
    public AdminPageAssert shouldHaveProductAddedNotification() {
        adminPage.getToastContainer().$(".toast")
                .shouldBe(visible)
                .shouldHave(exactText("Товар успешно добавлен!"));
        return this;
    }

    @Step("Проверить уведомление об обновлении товара {productId}")
    public AdminPageAssert shouldHaveProductUpdatedNotification(int productId) {
        adminPage.getToastContainer().$(".toast")
                .shouldBe(visible)
                .shouldHave(exactText("Товар #" + productId + " обновлен"));
        return this;
    }
}
