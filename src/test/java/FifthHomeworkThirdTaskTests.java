import com.codeborne.selenide.SelenideElement;
import org.example.Methods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

@Tag("ui")
@Tag("fifth_homework_third_task")
class FifthHomeworkThirdTaskTests {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ADMIN = "admin";
    private static final String PASSWORD = "secret123";

    // 3.6. Созданные в тестах товары удаляются в @AfterEach
    private final List<Integer> createdGoodIds = new ArrayList<>();

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
        Methods.tearDownSelenide();
    }

    // 3.5. Генерация тестовых данных через API
    private int createGoodViaApi(String name, double price) {
        int id = Methods.createGoodViaApi(name, price);
        createdGoodIds.add(id);
        return id;
    }

    private void loginToAdmin(String login, String password) {
        open("/admin");
        $(By.name("username")).shouldBe(visible).setValue(login);
        $(By.name("password")).setValue(password);
        $("form.login-form button").click();
    }

    private void addToCart(String productName, int qty) {
        SelenideElement card = $(".product-card[data-name='" + productName + "']").shouldBe(visible);
        card.$(".qty-input").setValue(String.valueOf(qty));
        card.$("button[data-action='add-to-cart']").click();
    }

    // 3.1. Добавить три единицы товара в корзину и оплатить их (сумма <= 300). Проверить уведомление об обработке заказа
    @Test
    void addThreeItemsAndPayOrderTest() {
        open("/");

        addToCart("товар1", 3); // 3 x 25 = 75 руб — в пределах лимита
        $("#open-cart-btn").click();
        $$("#cart-items .cart-item").shouldHave(size(1));
        $("#total-price").shouldHave(exactText("75"));

        $("#makeOrder").click();

        $("#main-title").shouldHave(text("Заказ успешно оформлен!"));
        $$("#toast-container .toast")
                .findBy(text("Заказ принят в обработку!"))
                .shouldBe(visible);
    }

    // 3.2. Добавить в корзину несколько разных товаров и проверить, что общая цена считается корректно
    // + генерация тестовых данных через апишку
    @Test
    void cartTotalPriceIsCalculatedCorrectlyTest() {
        String nameA = "Товар 6";
        String nameB = "Товар 7";
        int priceA = 30;
        int priceB = 45;
        createGoodViaApi(nameA, priceA);
        createGoodViaApi(nameB, priceB);

        open("/");

        int qtyA = 2;
        int qtyB = 1;
        addToCart(nameA, qtyA);
        addToCart(nameB, qtyB);

        int expectedTotal = priceA * qtyA + priceB * qtyB;

        $("#open-cart-btn").click();
        $$("#cart-items .cart-item").shouldHave(size(2));
        $("#total-price").shouldHave(exactText(String.valueOf(expectedTotal)));
        $("#cart-count").shouldHave(exactText(String.valueOf(qtyA + qtyB)));
    }

    // 3.3. Войти в админку и добавить товар. Проверить уведомление после добавления товара
    @Test
    void addProductInAdminShowsNotificationTest() {
        String name = "Товар-тост " + System.currentTimeMillis();

        loginToAdmin(ADMIN, PASSWORD);
        $("#n-name").shouldBe(visible).setValue(name);
        $("#n-price").setValue("10");
        $("#add-btn").click();

        $("#toast-container .toast").shouldBe(visible)
                .shouldHave(exactText("Товар успешно добавлен!"));

        // выписываем айдишки товаров чтобы удалить
        List<Integer> ids = given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .get(BASE_URL + "/goods/list")
                .then()
                .statusCode(200)
                .extract()
                .path("goods.findAll { it.name == '" + name + "' }.id");
        createdGoodIds.addAll(ids);
    }

    // 3.4. Войти в админку и отредактировать товар. Выйти на список товаров и проверить, что изменения применились
    //+ тестовые данные через апишку
    @Test
    void editProductInAdminAndCheckOnStorefrontTest() {
        int id = createGoodViaApi("Товар до правки", 40);
        String newName = "Товар после правки";

        loginToAdmin(ADMIN, PASSWORD);
        $("#nm-" + id).shouldBe(visible).setValue(newName);
        $("#pr-" + id).setValue("150");
        $("button[data-action='update'][data-id='" + id + "']").click();
        $("#toast-container .toast").shouldBe(visible)
                .shouldHave(exactText("Товар #" + id + " обновлен"));

        open("/");
        SelenideElement card = $(".product-card[data-name='" + newName + "']").shouldBe(visible);
        card.$("h4").shouldHave(exactText(newName));
        card.$("h4 + div").shouldHave(exactText("150 ₽"));
    }
}
