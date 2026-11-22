package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

// Базовые вызовы всех endpoints товаров без проверок
public class GoodsBasicApi {

    private static final String GOODS = "/goods";
    private static final String GOOD_BY_ID = GOODS + "/{id}";
    private static final String ADD_GOOD = GOODS + "/add";
    private static final String GOODS_LIST = GOODS + "/list";

    @Step("GET /goods/list")
    public Response getGoodsList() {
        return given()
                .spec(RestApiBuilder.authorized())
                .when()
                .get(GOODS_LIST);
    }

    @Step("GET /goods/{id}")
    public Response getGood(int id) {
        return given()
                .spec(RestApiBuilder.authorized())
                .pathParam("id", id)
                .when()
                .get(GOOD_BY_ID);
    }

    @Step("POST /goods/add с названием '{name}' и ценой {price}")
    public Response addGood(String name, double price) {
        return addGood(Map.of("name", name, "price", price));
    }

    @Step("POST /goods/add с произвольным телом")
    public Response addGood(Map<String, Object> body) {
        return given()
                .spec(RestApiBuilder.authorized())
                .body(body)
                .when()
                .post(ADD_GOOD);
    }

    @Step("PATCH /goods/{id}")
    public Response updateGood(int id, Map<String, Object> body) {
        return given()
                .spec(RestApiBuilder.authorized())
                .pathParam("id", id)
                .body(body)
                .when()
                .patch(GOOD_BY_ID);
    }

    @Step("DELETE /goods/{id}")
    public Response deleteGood(int id) {
        return given()
                .spec(RestApiBuilder.authorized())
                .pathParam("id", id)
                .when()
                .delete(GOOD_BY_ID);
    }

    @Step("Найти ID товара '{name}' через GET /goods/list")
    public int findGoodIdByName(String name) {
        List<Integer> ids = getGoodsList().jsonPath()
                .getList("goods.findAll { it.name == '" + name + "' }.id");
        return ids.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Товар не найден на стенде: " + name));
    }
}
