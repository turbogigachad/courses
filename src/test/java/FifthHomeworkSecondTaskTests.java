import com.codeborne.selenide.SelenideElement;
import org.example.Methods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

@Tag("ui")
@Tag("selenide_homework")
class FifthHomeworkSecondTaskTests {

    private static final String ADMIN = "admin";
    private static final String PASSWORD = "secret123";

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

    // Вход в админку
    private void loginToAdmin(String login, String password) {
        open("/admin");
        $(By.name("username")).shouldBe(visible).setValue(login);
        $(By.name("password")).setValue(password);
        $("form.login-form button").click();
    }

    // 2.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается
    @Test
    void addProductViaAdminAndCheckOnStorefrontTest() {
        createdGoodName = "товар " + System.currentTimeMillis();

        loginToAdmin(ADMIN, PASSWORD);
        $("#n-name").shouldBe(visible).setValue(createdGoodName);
        $("#n-price").setValue("99");
        $("#add-btn").click();
        $("#toast-container").shouldHave(text("Товар успешно добавлен"));

        open("/");
        $(".product-card[data-name='" + createdGoodName + "'] h4")
                .shouldBe(visible)
                .shouldHave(exactText(createdGoodName));
    }

    // 2.2. Добавить товар в корзину и проверить, что он отображается
    @Test
    void addProductToCartAndCheckItIsShownTest() {
        open("/");

        SelenideElement firstCard = $$(".product-card").first().shouldBe(visible);
        String productName = firstCard.attr("data-name");
        firstCard.$("button[data-action='add-to-cart']").click();

        $("#open-cart-btn").click();

        $$("#cart-items .cart-item").shouldHave(size(1));
        $("#cart-items .cart-item").shouldBe(visible).shouldHave(text(productName));
    }

    // 2.3. Попытаться войти в админку с неверным логином и паролем
    @Test
    void loginToAdminWithWrongCredentialsTest() {
        loginToAdmin("wrong_user", "wrong_password");

        $("div.alert-danger").shouldBe(visible)
                .shouldHave(exactText("Неверные учетные данные пользователя"));
        $("form.login-form").shouldBe(visible);
    }

    // 2.4. Проверить сохранение товаров в корзине после обновления страницы
    @Tag("bug")
    @Test
    void cartShouldKeepProductsAfterPageRefreshTest() {
        open("/");

        SelenideElement firstCard = $$(".product-card").first().shouldBe(visible);
        firstCard.$("button[data-action='add-to-cart']").click();
        $("#cart-count").shouldHave(exactText("1"));

        refresh();
        $("#open-cart-btn").click();

        $$("#cart-items .cart-item").shouldHave(size(1));
    }

    // 2.5. Добавить в корзину товаров более чем на 300 рублей и нажать «Оформить заказ»
    @Test
    void orderOver300RublesShowsJsAlertTest() {
        open("/");

        SelenideElement card = $(".product-card[data-name='Стакан']").shouldBe(visible);
        card.$(".qty-input").setValue("5");
        card.$("button[data-action='add-to-cart']").click();

        $("#open-cart-btn").click();
        $("#total-price").shouldHave(exactText("335"));

        $("#makeOrder").click();

        confirm("[SmartShop]: Денег не хватает! Сумма 335 ₽ превышает лимит 300 ₽.");
    }
}
