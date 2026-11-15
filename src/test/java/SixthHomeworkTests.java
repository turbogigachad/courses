import com.codeborne.selenide.DragAndDropOptions;
import com.codeborne.selenide.SelenideElement;
import org.example.Methods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

@Tag("ui")
@Tag("sixth_homework")
class SixthHomeworkTests {

    @BeforeEach
    void setUp() {
        Methods.setUpSelenide();
    }

    @AfterEach
    void tearDown() {
        Methods.tearDownSelenide();
    }

    // 1.1. Перетащить элемент в корзину с помощью Drag-and-Drop
    @Test
    void dragAndDropProductToCartTest() {
        open("/");

        SelenideElement card = $$(".product-card").first().shouldBe(visible);
        String productName = card.attr("data-name");

        card.dragAndDrop(DragAndDropOptions.to($("#open-cart-btn")));

        $("#cart-count").shouldHave(exactText("1"));
        $("#open-cart-btn").click();
        $$("#cart-items .cart-item").shouldHave(size(1));
        $("#cart-items .cart-item").shouldBe(visible).shouldHave(text(productName));
    }

    // 1.2. Удалить добавленный элемент из корзины и проверить, что он там больше не отображается
    @Test
    void removeProductFromCartTest() {
        open("/");

        SelenideElement card = $$(".product-card").first().shouldBe(visible);
        String productName = card.attr("data-name");
        card.dragAndDrop(DragAndDropOptions.to($("#open-cart-btn")));

        $("#open-cart-btn").click();
        $$("#cart-items .cart-item").shouldHave(size(1));

        $("#cart-items .cart-item button[data-action='remove']").click();

        $$("#cart-items .cart-item").shouldHave(size(0));
        $("#empty-cart").shouldBe(visible).shouldHave(exactText("Пусто"));
        $("#cart-count").shouldHave(exactText("0"));
    }
}
