package org.example.api;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.Config;

// Единая точка сборки HTTP-спецификации для basicApi-классов
public final class RestApiBuilder {

    private RestApiBuilder() {
    }

    @Step("Собрать авторизованную спецификацию API")
    public static RequestSpecification authorized() {
        return new RequestSpecBuilder()
                .setBaseUri(Config.apiUrl())
                .setAuth(RestAssured.basic(Config.adminLogin(), Config.adminPassword()))
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
