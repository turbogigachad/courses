package org.example;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.restassured.http.ContentType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.Random;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

public class Methods {


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
        return IntStream.generate(() -> r.nextInt(101)) // 0..100
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

    // создание товара
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

    // удаление товара
    public static void deleteGoodViaApi(int id) {
        given()
                .auth().basic(Config.adminLogin(), Config.adminPassword())
                .when()
                .delete(Config.apiUrl() + "/goods/" + id);
    }

    // стартовый товар из конфига: создаем, если его нет на стенде
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

    // удаление всех товаров с указанным именем
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

    // создание ChromeDriver
    public static WebDriver setUpChrome() {
        Config.printConfig();
        ensureInitialProductViaApi();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,900", "--no-sandbox", "--disable-dev-shm-usage");
        printStart();
        return new ChromeDriver(options);
    }

    // вейтер
    public static WebDriverWait createWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofMillis(Config.elementTimeoutMs()));
    }

    // выход из драйвера
    public static void tearDown(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
        printEnd();
    }

    // сетап драйвера
    public static void setUpSelenide() {
        Config.printConfig();
        ensureInitialProductViaApi();
        Configuration.baseUrl = Config.standUrl();
        Configuration.headless = true;
        Configuration.browserSize = "1280x900";
        Configuration.timeout = Config.elementTimeoutMs();
        printStart();
    }

    // закрытие браузера
    public static void tearDownSelenide() {
        Selenide.closeWebDriver();
        printEnd();
    }
}
