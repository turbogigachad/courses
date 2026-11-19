import org.example.Config;
import org.example.Methods;
import org.example.steps.GoodsApiSteps;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Tag("api")
@Tag("fifth_homework")
class FourthHomeWorkSecondTaskApiTests {

    private static final int NON_EXISTENT_ID = 99999999;

    private final GoodsApiSteps apiSteps = new GoodsApiSteps();
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

    @io.qameta.allure.Step("Создать товар '{name}' и запомнить его для удаления")
    private int createGood(String name, double price) {
        int id = apiSteps.addGood(name, price);
        createdIds.add(id);
        return id;
    }

    @Test
    void addGoodValidDataReturns200Test() {
        createGood("Товар для добавления", 120.5);
    }

    @Test
    void addGoodWithoutNameReturns400Test() {
        apiSteps.checkAddGoodWithoutNameReturns400();
    }

    @Test
    void addGoodWithNegativePriceReturns400Test() {
        apiSteps.checkAddGoodWithNegativePriceReturns400();
    }

    @Test
    void getExistingGoodReturns200Test() {
        String name = Config.initialProductName();
        int id = apiSteps.findGoodIdByName(name);
        apiSteps.checkExistingGood(id, name, Config.initialProductPrice());
    }

    @Tag("bug")
    @Test
    // Приложение отдаёт 500 вместо 404, тест оставлен падающим для демонстрации бага
    void getNonExistentGoodReturns404Test() {
        apiSteps.checkNonExistentGoodReturns404(NON_EXISTENT_ID);
    }

    @Test
    void deleteExistingGoodReturns200Test() {
        int id = createGood("Товар для удаления", 10.0);

        apiSteps.checkDeleteGoodStatus(id, 200);
        apiSteps.checkGoodNotInList(id);
    }

    @Test
    void deleteNonExistentGoodReturns404Test() {
        apiSteps.checkDeleteGoodStatus(NON_EXISTENT_ID, 404);
    }

    @Test
    void patchExistingGoodReturns200Test() {
        int id = createGood("Товар до обновления", 50.0);
        apiSteps.checkUpdateGood(id, "Товар после обновления", 60.0);
    }

    @Test
    void patchGoodWithEmptyNameReturns400Test() {
        int id = createGood("Товар для невалидного патча", 30.0);
        apiSteps.checkUpdateGoodWithEmptyNameReturns400(id);
    }

    @Test
    void patchNonExistentGoodReturns404Test() {
        apiSteps.checkUpdateNonExistentGoodReturns404(NON_EXISTENT_ID);
    }

    @Test
    void getGoodsListReturns200Test() {
        int id = createGood("Товар для списка", 15.0);
        apiSteps.checkGoodsListReturns200AndContains(id);
    }
}
