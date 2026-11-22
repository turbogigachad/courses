import io.qameta.allure.Step;
import org.example.Config;
import org.example.Methods;
import org.example.api.ApiAssert;
import org.example.api.GoodsBasicApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag("api")
@Tag("fifth_homework")
class FourthHomeWorkSecondTaskApiTests {

    private final GoodsBasicApi goodsApi = new GoodsBasicApi();
    private final List<Integer> createdIds = new ArrayList<>();

    @BeforeEach
    void printStart() {
        Config.printConfig();
        Methods.printStart();
    }

    @AfterEach
    void cleanUp() {
        for (int id : createdIds) {
            Methods.deleteGoodViaApi(id);
        }
        createdIds.clear();
        Methods.printEnd();
    }

    @Step("Создать товар '{name}' и запомнить его для удаления")
    private int createGood(String name, double price) {
        int id = ApiAssert.assertGoodCreated(goodsApi.addGood(name, price), name);
        createdIds.add(id);
        return id;
    }

    @Tag("smoke")
    @Test
    void addGoodValidDataReturns200Test() {
        createGood("Товар для добавления", 120.5);
    }

    @Test
    void addGoodWithoutNameReturns400Test() {
        ApiAssert.assertBadRequest(
                goodsApi.addGood(Map.of("price", 10)),
                "name",
                "Name cannot be empty"
        );
    }

    @Test
    void addGoodWithNegativePriceReturns400Test() {
        ApiAssert.assertBadRequest(
                goodsApi.addGood("Товар с минусом", -1),
                "price",
                "Price can not be less, than 0"
        );
    }

    @Tag("smoke")
    @Test
    void getExistingGoodReturns200Test() {
        String name = Config.initialProductName();
        int id = goodsApi.findGoodIdByName(name);

        ApiAssert.assertGood(goodsApi.getGood(id), id, name, Config.initialProductPrice());
    }

    @Tag("bug")
    @Test
    // Приложение отдаёт 500 вместо 404, тест оставлен падающим для демонстрации бага
    void getNonExistentGoodReturns404Test() {
        ApiAssert.assertNotFound(goodsApi.getGood(Config.nonExistentId()));
    }

    @Tag("smoke")
    @Test
    void deleteExistingGoodReturns200Test() {
        int id = createGood("Товар для удаления", 10.0);

        ApiAssert.assertStatus(goodsApi.deleteGood(id), 200);
        ApiAssert.assertGoodNotInList(goodsApi.getGoodsList(), id);
    }

    @Test
    void deleteNonExistentGoodReturns404Test() {
        ApiAssert.assertNotFound(goodsApi.deleteGood(Config.nonExistentId()));
    }

    @Tag("smoke")
    @Test
    void patchExistingGoodReturns200Test() {
        int id = createGood("Товар до обновления", 50.0);
        String newName = "Товар после обновления";
        double newPrice = 60.0;

        ApiAssert.assertGoodUpdated(
                goodsApi.updateGood(id, Map.of("name", newName, "price", newPrice)),
                id,
                newName,
                newPrice
        );
    }

    @Test
    void patchGoodWithEmptyNameReturns400Test() {
        int id = createGood("Товар для невалидного патча", 30.0);

        ApiAssert.assertBadRequest(
                goodsApi.updateGood(id, Map.of("name", "")),
                "name",
                "Name cannot be empty"
        );
    }

    @Test
    void patchNonExistentGoodReturns404Test() {
        ApiAssert.assertNotFound(goodsApi.updateGood(
                Config.nonExistentId(),
                Map.of("name", "Новое имя", "price", 1)
        ));
    }

    @Tag("smoke")
    @Test
    void getGoodsListReturns200Test() {
        int id = createGood("Товар для списка", 15.0);

        ApiAssert.assertGoodsListContains(goodsApi.getGoodsList(), id);
    }
}
