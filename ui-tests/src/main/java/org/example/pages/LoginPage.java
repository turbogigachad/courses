package org.example.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

// Форма авторизации в адм панели
public class LoginPage {

    private final SelenideElement loginInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement signInButton = $("form.login-form button[type='submit']");
    private final SelenideElement loginForm = $("form.login-form");
    private final SelenideElement errorAlert = $("div.alert-danger");

    @Step("Открыть форму авторизации")
    public LoginPage openPage() {
        open("/login");
        return shouldBeOpened();
    }

    @Step("Открыть форму авторизации админки")
    public LoginPage openAdminPage() {
        open("/admin");
        return shouldBeOpened();
    }

    @Step("Проверить видимость элементов формы авторизации")
    public LoginPage shouldBeOpened() {
        loginInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        signInButton.shouldBe(visible);
        return this;
    }

    @Step("Ввести логин '{login}'")
    public LoginPage enterLogin(String login) {
        loginInput.shouldBe(visible)
                .setValue(login)
                .shouldHave(value(login));
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage enterPassword(String password) {
        passwordInput.shouldBe(visible)
                .setValue(password)
                .shouldHave(value(password));
        return this;
    }

    @Step("Нажать кнопку входа")
    public LoginPage clickSignIn() {
        signInButton.shouldBe(visible).click();
        return this;
    }

    @Step("Выполнить вход с логином '{login}'")
    public LoginPage login(String login, String password) {
        enterLogin(login);
        enterPassword(password);
        clickSignIn();
        return this;
    }

    public SelenideElement getLoginForm() {
        return loginForm;
    }

    public SelenideElement getErrorAlert() {
        return errorAlert;
    }
}
