import io.restassured.http.ContentType;
import org.example.Methods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@Tag("api")
@Tag("fifth_homework")
class FourthHomeWorkSecondTaskApiTests {

    private static final String BASE_URI = org.example.Config.apiUrl();
    private static final String ADMIN = org.example.Config.adminLogin();
    private static final String PASSWORD = org.example.Config.adminPassword();
    private static final int NON_EXISTENT_ID = 99999999;

    private final List<Integer> createdIds = new ArrayList<>();

    @BeforeEach
    void printStart() {
        org.example.Config.printConfig();
        Methods.printStart();
    }

    @AfterEach
    void cleanUp() {
        for (int id : createdIds) {
            given()
                    .auth().basic(ADMIN, PASSWORD)
                    .when()
                    .delete(BASE_URI + "/goods/" + id);
        }
        createdIds.clear();
        Methods.printEnd();
    }

    private int addGood(String name, double price) {
        int id = given()
                .auth().basic(ADMIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "price", price))
                .when()
                .post(BASE_URI + "/goods/add")
                .then()
                .statusCode(200)
                .extract()
                .path("data.id");
        createdIds.add(id);
        return id;
    }

    @Test
    void addGoodValidDataReturns200Test() {
        int id = addGood("Товар для добавления", 120.5);

        given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .get(BASE_URI + "/goods/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Товар для добавления"));
    }

    @Test
    void addGoodWithoutNameReturns400Test() {
        given()
                .auth().basic(ADMIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"price\":10}")
                .when()
                .post(BASE_URI + "/goods/add")
                .then()
                .statusCode(400)
                .body("name", equalTo("Name cannot be empty"));
    }

    @Test
    void addGoodWithNegativePriceReturns400Test() {
        given()
                .auth().basic(ADMIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Товар с минусом\",\"price\":-1}")
                .when()
                .post(BASE_URI + "/goods/add")
                .then()
                .statusCode(400)
                .body("price", equalTo("Price can not be less, than 0"));
    }

    @Test
    void getExistingGoodReturns200Test() {
        int id = addGood("Товар для чтения", 77.0);

        given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .get(BASE_URI + "/goods/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("Товар для чтения"))
                .body("price", equalTo(77.0f));
    }

    @Tag("bug")
    @Test
    //только прила отдает 500, я оставил падающий, в целом чтобы он зеленый стал можно код поменять на 500
    void getNonExistentGoodReturns404Test() {
        given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .get(BASE_URI + "/goods/" + NON_EXISTENT_ID)
                .then()
                .statusCode(404);
    }

    @Test
    void deleteExistingGoodReturns200Test() {
        int id = addGood("Товар для удаления", 10.0);

        given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .delete(BASE_URI + "/goods/" + id)
                .then()
                .statusCode(200);

        given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .get(BASE_URI + "/goods/list")
                .then()
                .statusCode(200)
                .body("goods.id", not(hasItem(id)));
    }

    @Test
    void deleteNonExistentGoodReturns404Test() {
        given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .delete(BASE_URI + "/goods/" + NON_EXISTENT_ID)
                .then()
                .statusCode(404);
    }

    @Test
    void patchExistingGoodReturns200Test() {
        int id = addGood("Товар до обновления", 50.0);

        given()
                .auth().basic(ADMIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Товар после обновления\",\"price\":60.0}")
                .when()
                .patch(BASE_URI + "/goods/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("Товар после обновления"))
                .body("price", equalTo(60.0f));
    }

    @Test
    void patchGoodWithEmptyNameReturns400Test() {
        int id = addGood("Товар для невалидного патча", 30.0);

        given()
                .auth().basic(ADMIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"\"}")
                .when()
                .patch(BASE_URI + "/goods/" + id)
                .then()
                .statusCode(400)
                .body("name", equalTo("Name cannot be empty"));
    }

    @Test
    void patchNonExistentGoodReturns404Test() {
        given()
                .auth().basic(ADMIN, PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Новое имя\",\"price\":1}")
                .when()
                .patch(BASE_URI + "/goods/" + NON_EXISTENT_ID)
                .then()
                .statusCode(404);
    }

    @Test
    void getGoodsListReturns200Test() {
        int id = addGood("Товар для списка", 15.0);

        given()
                .auth().basic(ADMIN, PASSWORD)
                .when()
                .get(BASE_URI + "/goods/list")
                .then()
                .statusCode(200)
                .body("goods", notNullValue())
                .body("goods.id", hasItem(id));
    }
}
