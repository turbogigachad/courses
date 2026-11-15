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
import java.util.stream.Stream;
import java.util.Random;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

public class Methods {

    private static final String SHOP_BASE_URL = "http://localhost:8080";
    private static final String SHOP_ADMIN = "admin";
    private static final String SHOP_PASSWORD = "secret123";

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
                .auth().basic(SHOP_ADMIN, SHOP_PASSWORD)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + name + "\",\"price\":" + price + "}")
                .when()
                .post(SHOP_BASE_URL + "/goods/add")
                .then()
                .statusCode(200)
                .extract()
                .path("data.id");
    }

    // удаление товара
    public static void deleteGoodViaApi(int id) {
        given()
                .auth().basic(SHOP_ADMIN, SHOP_PASSWORD)
                .when()
                .delete(SHOP_BASE_URL + "/goods/" + id);
    }

    // удаление всех товаров с указанным именем
    public static void deleteGoodsByNameViaApi(String name) {
        List<Integer> ids = given()
                .auth().basic(SHOP_ADMIN, SHOP_PASSWORD)
                .when()
                .get(SHOP_BASE_URL + "/goods/list")
                .then()
                .statusCode(200)
                .extract()
                .path("goods.findAll { it.name == '" + name + "' }.id");
        for (int id : ids) {
            deleteGoodViaApi(id);
        }
    }

    // setUp для Selenium-тестов: создание ChromeDriver
    public static WebDriver setUpChrome() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,900", "--no-sandbox", "--disable-dev-shm-usage");
        printStart();
        return new ChromeDriver(options);
    }

    // явное ожидание для Selenium-тестов
    public static WebDriverWait createWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // tearDown для Selenium-тестов: закрытие браузера
    public static void tearDown(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
        printEnd();
    }

    // setUp для Selenide-тестов: настройка Configuration
    public static void setUpSelenide() {
        Configuration.baseUrl = SHOP_BASE_URL;
        Configuration.headless = true;
        Configuration.browserSize = "1280x900";
        Configuration.timeout = 10_000;
        printStart();
    }

    // tearDown для Selenide-тестов: закрытие браузера
    public static void tearDownSelenide() {
        Selenide.closeWebDriver();
        printEnd();
    }
}
