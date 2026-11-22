package org.example.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.data.Offset;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public final class ApiAssert {

    private ApiAssert() {
    }

    @Step("Проверить HTTP-код ответа: {expectedStatus}")
    public static void assertStatus(Response response, int expectedStatus) {
        response.then().statusCode(expectedStatus);
    }

    @Step("Проверить успешное создание товара '{name}'")
    public static int assertGoodCreated(Response response, String name) {
        assertStatus(response, 200);
        response.then()
                .body("message", equalTo("success"))
                .body("data.id", notNullValue());
        return response.jsonPath().getInt("data.id");
    }

    @Step("Проверить данные товара {id}: название '{name}', цена {price}")
    public static void assertGood(Response response, int id, String name, double price) {
        assertStatus(response, 200);
        response.then()
                .body("id", equalTo(id))
                .body("name", equalTo(name))
                .body("price", equalTo((float) price));
    }

    @Step("Проверить обновление товара {id}: название '{name}', цена {price}")
    public static void assertGoodUpdated(Response response, int id, String name, double price) {
        assertGood(response, id, name, price);
    }

    @Step("Проверить пустой список товаров")
    public static void assertGoodsListEmpty(Response response) {
        assertStatus(response, 200);
        response.then().body("goods", empty());
    }

    @Step("Проверить наличие товара {id} в списке")
    public static void assertGoodsListContains(Response response, int id) {
        assertStatus(response, 200);
        response.then()
                .body("goods", notNullValue())
                .body("goods.id", hasItem(id));
    }

    @Step("Проверить в списке товар {id} с названием '{name}' и ценой {price}")
    public static void assertGoodsListContains(Response response, int id, String name, double price) {
        assertStatus(response, 200);
        response.then()
                .body("goods.id", hasItem(id))
                .body("goods.name", hasItem(name))
                .body("goods.price", hasItem((float) price));
    }

    @Step("Проверить товар {id} в списке через AssertJ")
    public static void assertGoodsListContainsWithAssertJ(Response response, int id, String name, double price) {
        assertStatus(response, 200);
        List<Map<String, Object>> goods = response.jsonPath().getList("goods");
        assertThat(goods)
                .as("Список должен содержать товар id=%d, name=%s", id, name)
                .anySatisfy(good -> {
                    assertThat(((Number) good.get("id")).intValue()).isEqualTo(id);
                    assertThat(good.get("name")).isEqualTo(name);
                    assertThat(((Number) good.get("price")).doubleValue())
                            .isCloseTo(price, Offset.offset(0.001));
                });
    }

    @Step("Проверить отсутствие товара {id} в списке")
    public static void assertGoodNotInList(Response response, int id) {
        assertStatus(response, 200);
        response.then().body("goods.id", not(hasItem(id)));
    }

    @Step("Проверить ошибку 400: поле '{field}', сообщение '{message}'")
    public static void assertBadRequest(Response response, String field, String message) {
        assertStatus(response, 400);
        response.then().body(field, equalTo(message));
    }

    @Step("Проверить ошибку 404")
    public static void assertNotFound(Response response) {
        assertStatus(response, 404);
    }
}
