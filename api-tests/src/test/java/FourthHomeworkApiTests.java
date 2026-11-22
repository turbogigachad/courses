import io.qameta.allure.Step;
import org.example.Config;
import org.example.Methods;
import org.example.api.ApiAssert;
import org.example.api.GoodsBasicApi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@Tag("api")
@Tag("fourth_homework")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FourthHomeworkApiTests {

    private final GoodsBasicApi goodsApi = new GoodsBasicApi();
    private int createdGoodId = -1;

    @BeforeEach
    void printStart() {
        Config.printConfig();
        Methods.printStart();
    }

    @AfterEach
    void cleanUp() {
        if (createdGoodId > 0) {
            Methods.deleteGoodViaApi(createdGoodId);
            createdGoodId = -1;
        }
        Methods.printEnd();
    }

    @Step("Создать товар '{name}' и запомнить его для удаления")
    private int createGood(String name, double price) {
        createdGoodId = ApiAssert.assertGoodCreated(goodsApi.addGood(name, price), name);
        return createdGoodId;
    }

    @Test
    @Order(1)
    @Tag("requires_empty_catalog")
    void getGoodsListEmptyWithGivenWhenThenTest() {
        ApiAssert.assertGoodsListEmpty(goodsApi.getGoodsList());
    }

    @Test
    @Order(2)
    @Tag("requires_empty_catalog")
    void getGoodsListEmptyWithRequestSpecificationTest() {
        ApiAssert.assertGoodsListEmpty(goodsApi.getGoodsList());
    }

    @Test
    @Order(3)
    void addGoodAndFindItInListWithRestAssuredChecksTest() {
        String name = "Хлеб бородинский " + System.currentTimeMillis();
        double price = 55.0;
        createGood(name, price);

        ApiAssert.assertGoodsListContains(goodsApi.getGoodsList(), createdGoodId, name, price);
    }

    @Test
    @Order(4)
    void addGoodAndFindItInListWithAssertJTest() {
        String name = "Молоко 3.2 литра " + System.currentTimeMillis();
        double price = 89.9;
        createGood(name, price);

        ApiAssert.assertGoodsListContainsWithAssertJ(goodsApi.getGoodsList(), createdGoodId, name, price);
    }
}
