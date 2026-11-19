package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.Config;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class GoodsApiSteps {

    @Step("POST /goods/add: создать товар '{name}' с ценой {price} и проверить ответ")
    public int addGood(String name, double price) {
        return given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "price", price))
                .when()
                .post(Config.apiUrl() + "/goods/add")
                .then()
                .statusCode(200)
                .body("message", equalTo("success"))
                .extract()
                .path("data.id");
    }

    @Step("GET /goods/list: проверить пустой список через given/when/then")
    public void checkGoodsListEmptyWithGivenWhenThen() {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/list")
                .then()
                .statusCode(200)
                .body("goods", empty());
    }

    @Step("GET /goods/list: проверить пустой список через RequestSpecification")
    public void checkGoodsListEmptyWithRequestSpecification() {
        RequestSpecification spec = new RequestSpecBuilder()
                .setBaseUri(Config.apiUrl())
                .setAuth(io.restassured.RestAssured.basic(Config.adminLogin(), Config.adminPassword()))
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

    @Step("GET /goods/list: найти товар {id} встроенными проверками REST Assured")
    public void checkGoodsListContainsWithRestAssured(int id, String name, double price) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/list")
                .then()
                .statusCode(200)
                .body("goods.id", hasItem(id))
                .body("goods.name", hasItem(name))
                .body("goods.price", hasItem((float) price));
    }

    @Step("GET /goods/list: найти товар {id} через AssertJ")
    public void checkGoodsListContainsWithAssertJ(int id, String name, double price) {
        Response response = given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/list");

        List<Map<String, Object>> goods = response.jsonPath().getList("goods");
        assertThat(goods)
                .as("Список товаров должен содержать созданный товар id=%d, name=%s", id, name)
                .anySatisfy(good -> {
                    assertThat(((Number) good.get("id")).intValue()).isEqualTo(id);
                    assertThat(good.get("name")).isEqualTo(name);
                    assertThat(((Number) good.get("price")).doubleValue())
                            .isCloseTo(price, org.assertj.core.data.Offset.offset(0.001));
                });
    }

    @Step("GET /goods/{id}: проверить добавленный товар '{name}'")
    public void checkAddedGood(int id, String name) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/" + id)
                .then()
                .statusCode(200)
                .body("name", equalTo(name));
    }

    @Step("POST /goods/add: проверить ошибку 400 для товара без названия")
    public void checkAddGoodWithoutNameReturns400() {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .contentType(ContentType.JSON)
                .body("{\"price\":10}")
                .when()
                .post(Config.apiUrl() + "/goods/add")
                .then()
                .statusCode(400)
                .body("name", equalTo("Name cannot be empty"));
    }

    @Step("POST /goods/add: проверить ошибку 400 для отрицательной цены")
    public void checkAddGoodWithNegativePriceReturns400() {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Товар с минусом\",\"price\":-1}")
                .when()
                .post(Config.apiUrl() + "/goods/add")
                .then()
                .statusCode(400)
                .body("price", equalTo("Price can not be less, than 0"));
    }

    @Step("GET /goods/list: найти ID товара '{name}'")
    public int findGoodIdByName(String name) {
        List<Integer> ids = given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/list")
                .then()
                .statusCode(200)
                .extract()
                .path("goods.findAll { it.name == '" + name + "' }.id");
        assertThat(ids).as("Товар '%s' должен существовать на стенде", name).isNotEmpty();
        return ids.get(0);
    }

    @Step("GET /goods/{id}: проверить товар '{name}', его цену и код 200")
    public void checkExistingGood(int id, String name, double price) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo(name))
                .body("price", equalTo((float) price));
    }

    @Step("GET /goods/{id}: проверить код 404 для несуществующего товара")
    public void checkNonExistentGoodReturns404(int id) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/" + id)
                .then()
                .statusCode(404);
    }

    @Step("DELETE /goods/{id}: проверить код ответа {expectedStatus}")
    public void checkDeleteGoodStatus(int id, int expectedStatus) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .delete(Config.apiUrl() + "/goods/" + id)
                .then()
                .statusCode(expectedStatus);
    }

    @Step("GET /goods/list: проверить, что товар {id} удалён")
    public void checkGoodNotInList(int id) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/list")
                .then()
                .statusCode(200)
                .body("goods.id", not(hasItem(id)));
    }

    @Step("PATCH /goods/{id}: обновить товар и проверить новые данные")
    public void checkUpdateGood(int id, String newName, double newPrice) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .contentType(ContentType.JSON)
                .body(Map.of("name", newName, "price", newPrice))
                .when()
                .patch(Config.apiUrl() + "/goods/" + id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo(newName))
                .body("price", equalTo((float) newPrice));
    }

    @Step("PATCH /goods/{id}: проверить ошибку 400 для пустого названия")
    public void checkUpdateGoodWithEmptyNameReturns400(int id) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"\"}")
                .when()
                .patch(Config.apiUrl() + "/goods/" + id)
                .then()
                .statusCode(400)
                .body("name", equalTo("Name cannot be empty"));
    }

    @Step("PATCH /goods/{id}: проверить код 404 для несуществующего товара")
    public void checkUpdateNonExistentGoodReturns404(int id) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .contentType(ContentType.JSON)
                .body("{\"name\":\"Новое имя\",\"price\":1}")
                .when()
                .patch(Config.apiUrl() + "/goods/" + id)
                .then()
                .statusCode(404);
    }

    @Step("GET /goods/list: проверить код 200 и наличие товара {id}")
    public void checkGoodsListReturns200AndContains(int id) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/list")
                .then()
                .statusCode(200)
                .body("goods", notNullValue())
                .body("goods.id", hasItem(id));
    }
}
