package org.example.steps;

import io.qameta.allure.Step;
import org.example.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SeleniumShopSteps {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public SeleniumShopSteps(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    @Step("Открыть админку и выполнить вход с логином '{login}'")
    public void loginToAdmin(String login, String password) {
        driver.get(Config.standUrl() + "/admin");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                .sendKeys(login);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("form.login-form button[type='submit'], form.login-form button")).click();
    }

    @Step("Добавить через админку товар '{name}' с ценой {price} и проверить уведомление")
    public void addProduct(String name, String price) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("n-name")))
                .sendKeys(name);
        driver.findElement(By.id("n-price")).sendKeys(price);
        driver.findElement(By.id("add-btn")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("toast-container"), "Товар успешно добавлен"));
    }

    @Step("Открыть витрину магазина")
    public void openStorefront() {
        driver.get(Config.standUrl() + "/");
    }

    @Step("Проверить товар '{expectedName}' на витрине")
    public void checkProductOnStorefront(String expectedName) {
        WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".product-card[data-name='" + expectedName + "'] h4")));
        assertTrue(productName.isDisplayed(),
                "Ожидалось: товар '" + expectedName + "' отображается на витрине, факт: элемент не найден");
        assertEquals(expectedName, productName.getText(),
                "Название товара на витрине не совпадает с созданным");
    }

    @Step("Добавить первый товар витрины в корзину")
    public String addFirstProductToCart() {
        WebElement firstCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".product-card")));
        String productName = firstCard.getAttribute("data-name");
        firstCard.findElement(By.cssSelector("button[data-action='add-to-cart']")).click();
        return productName;
    }

    @Step("Открыть корзину")
    public void openCart() {
        driver.findElement(By.id("open-cart-btn")).click();
    }

    @Step("Проверить товар '{productName}' в корзине")
    public void checkProductInCart(String productName) {
        List<WebElement> cartItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("#cart-items .cart-item")));
        assertEquals(1, cartItems.size(),
                "Ожидалась 1 карточка товара в корзине, факт: " + cartItems.size());
        assertTrue(cartItems.get(0).getText().contains(productName),
                "Ожидалось: в корзине товар '" + productName + "', факт: '" + cartItems.get(0).getText() + "'");
    }

    @Step("Проверить сообщение об ошибке входа")
    public void checkInvalidLoginError() {
        WebElement errorAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.alert-danger")));
        assertEquals("Неверные учетные данные пользователя", errorAlert.getText(),
                "Ожидалось сообщение об ошибке входа, факт: '" + errorAlert.getText() + "'");
        assertFalse(driver.getPageSource().contains("SmartShop Admin"),
                "Пользователь вошел в систему");
    }

    @Step("Проверить счётчик корзины: {expectedCount}")
    public void checkCartCount(String expectedCount) {
        wait.until(ExpectedConditions.textToBe(By.id("cart-count"), expectedCount));
    }

    @Step("Обновить страницу магазина")
    public void refreshPage() {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-card")));
    }

    @Step("Проверить после обновления сохранение товара '{productName}' в корзине")
    public void checkCartItemsCountAfterRefresh(String productName) {
        List<WebElement> cartItems = driver.findElements(By.cssSelector("#cart-items .cart-item"));
        assertEquals(1, cartItems.size(),
                "Ожидалось: после обновления страницы в корзине остался товар '" + productName
                        + "', факт: товаров в корзине " + cartItems.size());
    }
}
