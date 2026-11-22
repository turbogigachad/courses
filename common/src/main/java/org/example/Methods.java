package org.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class Methods {

    static {
        RestAssured.filters(new AllureRestAssured());
    }

    public static void printStart() {
        System.out.println("========================Test method start");
    }

    public static void printEnd() {
        System.out.println("Test method end");
        System.out.println("========================");
    }

    public static int getRandomNumberWithBound(int n) {
        Random random = new Random();
        return random.nextInt(n);
    }

    public static Stream<Integer> randomScores() {
        Random r = new Random();
        return IntStream.generate(() -> r.nextInt(101))
                .limit(10)
                .boxed();
    }

    public static void report(boolean condition, String message) {
        if (condition) {
            System.out.println("TEST PASSED: " + message);
        } else {
            System.out.println("TEST FAILED: " + message);
        }
    }

    @Step("POST /goods/add: создать товар '{name}' с ценой {price}")
    public static int createGoodViaApi(String name, double price) {
        return given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "price", price))
                .when()
                .post(Config.apiUrl() + "/goods/add")
                .then()
                .statusCode(200)
                .extract()
                .path("data.id");
    }

    @Step("DELETE /goods/{id}: удалить товар")
    public static void deleteGoodViaApi(int id) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .delete(Config.apiUrl() + "/goods/" + id);
    }

    @Step("Создать стартовый товар из конфигурации, если он отсутствует")
    public static void ensureInitialProductViaApi() {
        List<Integer> ids = given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/list")
                .then()
                .statusCode(200)
                .extract()
                .path("goods.findAll { it.name == '" + Config.initialProductName() + "' }.id");
        if (ids.isEmpty()) {
            createGoodViaApi(Config.initialProductName(), Config.initialProductPrice());
        }
    }

    @Step("Удалить все товары с названием '{name}'")
    public static void deleteGoodsByNameViaApi(String name) {
        List<Integer> ids = given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .get(Config.apiUrl() + "/goods/list")
                .then()
                .statusCode(200)
                .extract()
                .path("goods.findAll { it.name == '" + name + "' }.id");
        for (int id : ids) {
            deleteGoodViaApi(id);
        }
    }

    @Step("Запустить Chrome для Selenium-теста")
    public static WebDriver setUpChrome() {
        Config.printConfig();
        ensureInitialProductViaApi();
        ChromeOptions options = new ChromeOptions();
        if (Config.browserHeadless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=" + Config.browserSize(), "--no-sandbox", "--disable-dev-shm-usage");
        printStart();
        return new ChromeDriver(options);
    }

    public static WebDriverWait createWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofMillis(Config.elementTimeoutMs()));
    }

    @Step("Закрыть Selenium-браузер")
    public static void tearDown(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
        printEnd();
    }

    @Step("Настроить Selenide и Allure Selenide Listener")
    public static void setUpSelenide() {
        Config.printConfig();
        ensureInitialProductViaApi();
        Configuration.baseUrl = Config.standUrl();
        Configuration.browser = Config.browser();
        Configuration.headless = Config.browserHeadless();
        Configuration.browserSize = Config.browserSize();
        Configuration.timeout = Config.elementTimeoutMs();
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide().screenshots(true).savePageSource(true));
        printStart();
    }

    @Step("Закрыть Selenide-браузер")
    public static void tearDownSelenide() {
        Selenide.closeWebDriver();
        printEnd();
    }
}
