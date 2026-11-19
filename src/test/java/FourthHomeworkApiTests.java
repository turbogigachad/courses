import org.example.Config;
import org.example.Methods;
import org.example.steps.GoodsApiSteps;
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

    private final GoodsApiSteps apiSteps = new GoodsApiSteps();
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

    @Test
    @Order(1)
    @Tag("requires_empty_catalog")
    void getGoodsListEmptyWithGivenWhenThenTest() {
        apiSteps.checkGoodsListEmptyWithGivenWhenThen();
    }

    @Test
    @Order(2)
    @Tag("requires_empty_catalog")
    void getGoodsListEmptyWithRequestSpecificationTest() {
        apiSteps.checkGoodsListEmptyWithRequestSpecification();
    }

    @Test
    @Order(3)
    void addGoodAndFindItInListWithRestAssuredChecksTest() {
        String name = "Хлеб бородинский " + System.currentTimeMillis();
        double price = 55.0;
        createdGoodId = apiSteps.addGood(name, price);

        apiSteps.checkGoodsListContainsWithRestAssured(createdGoodId, name, price);
    }

    @Test
    @Order(4)
    void addGoodAndFindItInListWithAssertJTest() {
        String name = "Молоко 3.2 литра " + System.currentTimeMillis();
        double price = 89.9;
        createdGoodId = apiSteps.addGood(name, price);

        apiSteps.checkGoodsListContainsWithAssertJ(createdGoodId, name, price);
    }
}
