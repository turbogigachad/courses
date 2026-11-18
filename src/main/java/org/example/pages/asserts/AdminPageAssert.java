package org.example.pages.asserts;

import org.example.pages.AdminPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;

public class AdminPageAssert {

    private final AdminPage adminPage;

    public AdminPageAssert(AdminPage adminPage) {
        this.adminPage = adminPage;
    }

    public AdminPageAssert shouldHaveProductAddedNotification() {
        adminPage.getToastContainer().$(".toast")
                .shouldBe(visible)
                .shouldHave(exactText("Товар успешно добавлен!"));
        return this;
    }

    public AdminPageAssert shouldHaveProductUpdatedNotification(int productId) {
        adminPage.getToastContainer().$(".toast")
                .shouldBe(visible)
                .shouldHave(exactText("Товар #" + productId + " обновлен"));
        return this;
    }
}
