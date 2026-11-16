import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@Tag("api")
@Tag("fourth_homework")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FourthHomeworkApiTests {

    private static final String BASE_URI = "http://localhost:8080";
    private static final String ADMIN = "admin";
    private static final String PASSWORD = "secret123";

    private int createdGoodId = -1;

    @BeforeEach
    void printStart() {
        org.example.Methods.printStart();
    }

    @AfterEach
    void cleanUp() {
        if (createdGoodId > 0) {
            given()
                    .auth().basic(ADMIN, PASSWORD)
                    .when()
                    .delete(BASE_URI + "/goods/" + createdGoodId);
            createdGoodId = -1;
        }
        org.example.Methods.printEnd();
    }

    private int addGood(String name, double price) {
        return given()
                .auth().basic(ADMIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "price", price))
                .when()
                .post(BASE_URI + "/goods/add")
                .then()
                .statusCode(200)
                .body("message", equalTo("success"))
                .extract()
                .path("data.id");
    }

    @Test
    @Order(1)
    void getGoodsListEmptyWithGivenWhenThenTest() {
        given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .get(BASE_URI + "/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    @Test
    @Order(2)
    void getGoodsListEmptyWithRequestSpecificationTest() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setAuth(io.restassured.RestAssured.basic(ADMIN, PASSWORD))
                .setContentType(ContentType.JSON)
                .build();

        given()
                .spec(spec)
                .when()
                .get("/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    @Test
    @Order(3)
    void addGoodAndFindItInListWithRestAssuredChecksTest() {
        String name = "Хлеб бородинский " + System.currentTimeMillis();
        createdGoodId = addGood(name, 55.0);

        given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .get(BASE_URI + "/goods/list")
                .then()
                .statusCode(200)
                .body("goods.id", hasItem(createdGoodId))
                .body("goods.name", hasItem(name))
                .body("goods.price", hasItem(55.0f));
    }

    @Test
    @Order(4)
    void addGoodAndFindItInListWithAssertJTest() {
        String name = "Молоко 3.2 литра " + System.currentTimeMillis();
        double price = 89.9;
        createdGoodId = addGood(name, price);

        Response response = given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .get(BASE_URI + "/goods/list");

        List<Map<String, Object>> goods = response.jsonPath().getList("goods");

        assertThat(goods)
                .as("Список товаров должен содержать созданный товар id=%d, name=%s", createdGoodId, name)
                .anySatisfy(good -> {
                    assertThat(((Number) good.get("id")).intValue()).isEqualTo(createdGoodId);
                    assertThat(good.get("name")).isEqualTo(name);
                    assertThat(((Number) good.get("price")).doubleValue())
                            .isCloseTo(price, org.assertj.core.data.Offset.offset(0.001));
                });
    }
}
