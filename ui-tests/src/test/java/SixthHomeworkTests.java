import org.example.Methods;
import org.example.pages.MainPage;
import org.example.pages.asserts.MainPageAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
@Tag("sixth_homework")
class SixthHomeworkTests {

    private final MainPage mainPage = new MainPage();
    private final MainPageAssert mainPageAssert = new MainPageAssert(mainPage);

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
        mainPage.openPage();
        String productName = mainPage.dragFirstProductToCart();
        mainPageAssert.shouldHaveCartCount(1);

        mainPage.openCart();
        mainPageAssert.shouldHaveCartItemsCount(1)
                .shouldHaveCartItem(productName);
    }

    // 1.2. Удалить добавленный элемент из корзины и проверить, что он там больше не отображается
    @Test
    void removeProductFromCartTest() {
        mainPage.openPage();
        mainPage.dragFirstProductToCart();
        mainPage.openCart();
        mainPageAssert.shouldHaveCartItemsCount(1);

        mainPage.removeCartItem(0);
        mainPageAssert.shouldHaveEmptyCart();
    }
}
