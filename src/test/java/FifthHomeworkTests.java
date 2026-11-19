import org.example.Config;
import org.example.Methods;
import org.example.steps.SeleniumShopSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

@Tag("ui")
@Tag("selenium_homework")
class FifthHomeworkTests {

    private WebDriver driver;
    private SeleniumShopSteps shopSteps;
    private String createdGoodName;

    @BeforeEach
    void setUp() {
        driver = Methods.setUpChrome();
        WebDriverWait wait = Methods.createWait(driver);
        shopSteps = new SeleniumShopSteps(driver, wait);
    }

    @AfterEach
    void tearDown() {
        if (createdGoodName != null) {
            Methods.deleteGoodsByNameViaApi(createdGoodName);
            createdGoodName = null;
        }
        Methods.tearDown(driver);
    }

    // 1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается
    @Test
    void addProductViaAdminAndCheckOnStorefrontTest() {
        createdGoodName = "UI-товар " + System.currentTimeMillis();

        shopSteps.loginToAdmin(Config.adminLogin(), Config.adminPassword());
        shopSteps.addProduct(createdGoodName, "99");
        shopSteps.openStorefront();
        shopSteps.checkProductOnStorefront(createdGoodName);
    }

    // 1.2. Добавить товар в корзину и проверить, что он отображается
    @Test
    void addProductToCartAndCheckItIsShownTest() {
        shopSteps.openStorefront();
        String productName = shopSteps.addFirstProductToCart();
        shopSteps.openCart();
        shopSteps.checkProductInCart(productName);
    }

    // 1.3. Попытаться войти в админку с неверным логином и паролем
    @Test
    void loginToAdminWithWrongCredentialsTest() {
        shopSteps.loginToAdmin("wrong_user", "wrong_password");
        shopSteps.checkInvalidLoginError();
    }

    // 1.4. Проверить сохранение товаров в корзине после обновления страницы
    @Tag("bug") // Данные корзины не сохраняются после обновления
    @Test
    void cartShouldKeepProductsAfterPageRefreshTest() {
        shopSteps.openStorefront();
        String productName = shopSteps.addFirstProductToCart();
        shopSteps.checkCartCount("1");

        shopSteps.refreshPage();
        shopSteps.openCart();
        shopSteps.checkCartItemsCountAfterRefresh(productName);
    }
}
