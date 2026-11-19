package org.example.pages.asserts;

import io.qameta.allure.Step;
import org.example.pages.LoginPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;

public class LoginPageAssert {

    private final LoginPage loginPage;

    public LoginPageAssert(LoginPage loginPage) {
        this.loginPage = loginPage;
    }

    @Step("Проверить ошибку входа с неверными учётными данными")
    public LoginPageAssert shouldHaveInvalidCredentialsError() {
        loginPage.getErrorAlert()
                .shouldBe(visible)
                .shouldHave(exactText("Неверные учетные данные пользователя"));
        loginPage.getLoginForm().shouldBe(visible);
        return this;
    }
}
