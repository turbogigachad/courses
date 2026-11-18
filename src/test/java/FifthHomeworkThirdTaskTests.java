import org.example.Config;
import org.example.Methods;
import org.example.pages.AdminPage;
import org.example.pages.LoginPage;
import org.example.pages.MainPage;
import org.example.pages.asserts.AdminPageAssert;
import org.example.pages.asserts.MainPageAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Tag("ui")
@Tag("fifth_homework_third_task")
class FifthHomeworkThirdTaskTests {

    private final List<Integer> createdGoodIds = new ArrayList<>();
    private final List<String> createdGoodNames = new ArrayList<>();
    private final MainPage mainPage = new MainPage();
    private final MainPageAssert mainPageAssert = new MainPageAssert(mainPage);
    private final AdminPage adminPage = new AdminPage();
    private final AdminPageAssert adminPageAssert = new AdminPageAssert(adminPage);

    @BeforeEach
    void setUp() {
        Methods.setUpSelenide();
    }

    @AfterEach
    void tearDown() {
        for (int id : createdGoodIds) {
            Methods.deleteGoodViaApi(id);
        }
        createdGoodIds.clear();

        for (String name : createdGoodNames) {
            Methods.deleteGoodsByNameViaApi(name);
        }
        createdGoodNames.clear();
        Methods.tearDownSelenide();
    }

    // Генерация тестовых данных через API
    private int createGoodViaApi(String name, double price) {
        int id = Methods.createGoodViaApi(name, price);
        createdGoodIds.add(id);
        return id;
    }

    private void loginToAdmin() {
        new LoginPage().openAdminPage().login(Config.adminLogin(), Config.adminPassword());
    }

    // 2.1. Добавить три единицы товара в корзину и оплатить их (сумма <= 300). Проверить уведомление
    @Test
    void addThreeItemsAndPayOrderTest() {
        int quantity = 3;
        int expectedTotal = (int) (Config.initialProductPrice() * quantity);

        mainPage.openPage()
                .addProductToCart(Config.initialProductName(), quantity)
                .openCart();
        mainPageAssert.shouldHaveCartItemsCount(1)
                .shouldHaveTotalPrice(expectedTotal);

        mainPage.makeOrder();
        mainPageAssert.shouldHaveOrderProcessedNotification();
    }

    // 2.2. Добавить несколько разных товаров и проверить общую стоимость корзины
    @Test
    void cartTotalPriceIsCalculatedCorrectlyTest() {
        String suffix = String.valueOf(System.currentTimeMillis());
        String firstName = "Товар 6 " + suffix;
        String secondName = "Товар 7 " + suffix;
        int firstPrice = 30;
        int secondPrice = 45;
        createGoodViaApi(firstName, firstPrice);
        createGoodViaApi(secondName, secondPrice);

        int firstQuantity = 2;
        int secondQuantity = 1;
        int expectedTotal = firstPrice * firstQuantity + secondPrice * secondQuantity;

        mainPage.openPage()
                .addProductToCart(firstName, firstQuantity)
                .addProductToCart(secondName, secondQuantity)
                .openCart();

        mainPageAssert.shouldHaveCartItemsCount(2)
                .shouldHaveTotalPrice(expectedTotal)
                .shouldHaveCartCount(firstQuantity + secondQuantity);
    }

    // 2.3. Войти в админку, добавить товар и проверить уведомление
    @Test
    void addProductInAdminShowsNotificationTest() {
        String name = "Товар-тост " + System.currentTimeMillis();
        createdGoodNames.add(name);

        loginToAdmin();
        adminPage.addProduct(name, "10");

        adminPageAssert.shouldHaveProductAddedNotification();
    }

    // 2.4. Отредактировать товар в админке и проверить изменения на витрине
    @Test
    void editProductInAdminAndCheckOnStorefrontTest() {
        String suffix = String.valueOf(System.currentTimeMillis());
        int id = createGoodViaApi("Товар до правки " + suffix, 40);
        String newName = "Товар после правки " + suffix;

        loginToAdmin();
        adminPage.updateProduct(id, newName, "150");
        adminPageAssert.shouldHaveProductUpdatedNotification(id);

        MainPage storefrontPage = adminPage.returnToStorefront();
        new MainPageAssert(storefrontPage).shouldHaveProduct(newName, "150 ₽");
    }
}
