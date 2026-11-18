import org.example.Config;
import org.example.Methods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@Tag("selenium_homework")
class FifthHomeworkTests {


    private WebDriver driver;
    private WebDriverWait wait;
    private String createdGoodName;

    @BeforeEach
    void setUp() {
        driver = Methods.setUpChrome();
        wait = Methods.createWait(driver);
    }

    @AfterEach
    void tearDown() {
        if (createdGoodName != null) {
            Methods.deleteGoodsByNameViaApi(createdGoodName);
            createdGoodName = null;
        }
        Methods.tearDown(driver);
    }

    private void loginToAdmin(String login, String password) {
        driver.get(Config.standUrl() + "/admin");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")))
                .sendKeys(login);
        driver.findElement(By.name("password")).sendKeys(password);
        driver.findElement(By.cssSelector("form.login-form button[type='submit'], form.login-form button")).click();
    }

    // 1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается
    @Test
    void addProductViaAdminAndCheckOnStorefrontTest() {
        createdGoodName = "UI-товар " + System.currentTimeMillis();

        loginToAdmin(Config.adminLogin(), Config.adminPassword());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("n-name")))
                .sendKeys(createdGoodName);
        driver.findElement(By.id("n-price")).sendKeys("99");
        driver.findElement(By.id("add-btn")).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("toast-container"), "Товар успешно добавлен"));

        driver.get(Config.standUrl() + "/");

        WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".product-card[data-name='" + createdGoodName + "'] h4")));
        assertTrue(productName.isDisplayed(),
                "Ожидалось: товар '" + createdGoodName + "' отображается на витрине, факт: элемент не найден");
        assertEquals(createdGoodName, productName.getText(),
                "Название товара на витрине не совпадает с созданным");
    }

    // 1.2. Добавить товар в корзину и проверить, что он отображается
    @Test
    void addProductToCartAndCheckItIsShownTest() {
        driver.get(Config.standUrl() + "/");

        WebElement firstCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".product-card")));
        String productName = firstCard.getAttribute("data-name");
        firstCard.findElement(By.cssSelector("button[data-action='add-to-cart']")).click();

        driver.findElement(By.id("open-cart-btn")).click();

        List<WebElement> cartItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("#cart-items .cart-item")));
        assertEquals(1, cartItems.size(),
                "Ожидалась 1 карточка товара в корзине, факт: " + cartItems.size());
        assertTrue(cartItems.get(0).getText().contains(productName),
                "Ожидалось: в корзине товар '" + productName + "', факт: '" + cartItems.get(0).getText() + "'");
    }

    // 1.3. Попытаться войти в админку с неверным логином и паролем
    @Test
    void loginToAdminWithWrongCredentialsTest() {
        loginToAdmin("wrong_user", "wrong_password");

        WebElement errorAlert = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.alert-danger")));
        assertEquals("Неверные учетные данные пользователя", errorAlert.getText(),
                "Ожидалось сообщение об ошибке входа, факт: '" + errorAlert.getText() + "'");

        String pageSource = driver.getPageSource();
        assertFalse(pageSource.contains("SmartShop Admin"),
                "Пользователь вошел в систему");
    }

    // 1.4. Проверить сохранение товаров в корзине после обновления страницы
    @Tag("bug") //не остаются данные после рефреша
    @Test
    void cartShouldKeepProductsAfterPageRefreshTest() {
        driver.get(Config.standUrl() + "/");

        WebElement firstCard = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".product-card")));
        String productName = firstCard.getAttribute("data-name");
        firstCard.findElement(By.cssSelector("button[data-action='add-to-cart']")).click();
        wait.until(ExpectedConditions.textToBe(By.id("cart-count"), "1"));

        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-card")));
        driver.findElement(By.id("open-cart-btn")).click();

        List<WebElement> cartItems = driver.findElements(By.cssSelector("#cart-items .cart-item"));
        assertEquals(1, cartItems.size(),
                "Ожидалось: после обновления страницы в корзине остался товар '" + productName
                        + "', факт: товаров в корзине " + cartItems.size());
    }
}
