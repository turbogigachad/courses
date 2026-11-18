package org.example.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class LoginPage {

    private final SelenideElement loginInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement signInButton = $("form.login-form button[type='submit']");

    public LoginPage openPage() {
        open("/login");
        return shouldBeOpened();
    }

    public LoginPage openAdminPage() {
        open("/admin");
        return shouldBeOpened();
    }

    // Базовые проверки видимости всех элементов формы
    public LoginPage shouldBeOpened() {
        loginInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        signInButton.shouldBe(visible);
        return this;
    }

    public LoginPage enterLogin(String login) {
        loginInput.shouldBe(visible)
                .setValue(login)
                .shouldHave(value(login));
        return this;
    }

    public LoginPage enterPassword(String password) {
        passwordInput.shouldBe(visible)
                .setValue(password)
                .shouldHave(value(password));
        return this;
    }

    public LoginPage clickSignIn() {
        signInButton.shouldBe(visible).click();
        return this;
    }

    public LoginPage login(String login, String password) {
        enterLogin(login);
        enterPassword(password);
        clickSignIn();
        return this;
    }
}
