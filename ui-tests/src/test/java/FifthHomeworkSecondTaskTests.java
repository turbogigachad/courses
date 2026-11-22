import io.qameta.allure.Step;
import org.example.Config;
import org.example.Methods;
import org.example.pages.AdminPage;
import org.example.pages.LoginPage;
import org.example.pages.MainPage;
import org.example.pages.asserts.AdminPageAssert;
import org.example.pages.asserts.LoginPageAssert;
import org.example.pages.asserts.MainPageAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
@Tag("selenide_homework")
class FifthHomeworkSecondTaskTests {

    private final MainPage mainPage = new MainPage();
    private final MainPageAssert mainPageAssert = new MainPageAssert(mainPage);
    private final AdminPage adminPage = new AdminPage();
    private final AdminPageAssert adminPageAssert = new AdminPageAssert(adminPage);
    private String createdGoodName;

    @BeforeEach
    void setUp() {
        Methods.setUpSelenide();
    }

    @AfterEach
    void tearDown() {
        if (createdGoodName != null) {
            Methods.deleteGoodsByNameViaApi(createdGoodName);
            createdGoodName = null;
        }
        Methods.tearDownSelenide();
    }

    @Step("Войти в админку с логином '{login}'")
    private void loginToAdmin(String login, String password) {
        new LoginPage().openAdminPage().login(login, password);
    }

    // 2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается
    @Tag("smoke")
    @Test
    void addProductViaAdminAndCheckOnStorefrontTest() {
        createdGoodName = "товар " + System.currentTimeMillis();

        loginToAdmin(Config.adminLogin(), Config.adminPassword());
        adminPage.addProduct(createdGoodName, "99");
        adminPageAssert.shouldHaveProductAddedNotification();

        mainPage.openPage();
        mainPageAssert.shouldHaveProduct(createdGoodName, "99 ₽");
    }

    // 2.2. Добавить товар в корзину и проверить, что он отображается
    @Tag("smoke")
    @Test
    void addProductToCartAndCheckItIsShownTest() {
        mainPage.openPage();
        String productName = mainPage.addFirstProductToCart();
        mainPage.openCart();

        mainPageAssert.shouldHaveCartItemsCount(1)
                .shouldHaveCartItem(productName);
    }

    // 2.3. Попытаться войти в админку с неверным логином и паролем
    @Tag("smoke")
    @Test
    void loginToAdminWithWrongCredentialsTest() {
        LoginPage loginPage = new LoginPage();
        loginPage.openAdminPage().login("wrong_user", "wrong_password");

        new LoginPageAssert(loginPage).shouldHaveInvalidCredentialsError();
    }

    // 2.4. Проверить сохранение товаров в корзине после обновления страницы
    @Tag("bug")
    @Test
    void cartShouldKeepProductsAfterPageRefreshTest() {
        mainPage.openPage();
        mainPage.addFirstProductToCart();
        mainPageAssert.shouldHaveCartCount(1);

        mainPage.refreshPage().openCart();
        mainPageAssert.shouldHaveCartItemsCount(1);
    }

    // 2.5. Добавить в корзину товаров сверх лимита и нажать «Оформить заказ»
    @Tag("smoke")
    @Test
    void orderOverLimitShowsJsAlertTest() {
        int quantity = Config.orderLimit() / (int) Config.initialProductPrice() + 1;
        int expectedTotal = (int) (Config.initialProductPrice() * quantity);

        mainPage.openPage()
                .addProductToCart(Config.initialProductName(), quantity)
                .openCart();
        mainPageAssert.shouldHaveTotalPrice(expectedTotal);

        mainPage.makeOrder();
        mainPageAssert.shouldHaveOrderOverLimitAlert(expectedTotal);
    }
}
