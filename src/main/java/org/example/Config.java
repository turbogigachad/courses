package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Config {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new IllegalStateException("Файл config.properties не найден в resources");
            }
            PROPS.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать config.properties", e);
        }
    }

    public static String standUrl() {
        return PROPS.getProperty("stand.url");
    }

    public static String apiUrl() {
        return PROPS.getProperty("api.url");
    }

    public static long elementTimeoutMs() {
        return Long.parseLong(PROPS.getProperty("element.timeout.ms", "10000"));
    }

    public static String loggingMode() {
        return PROPS.getProperty("logging.mode", "console");
    }

    public static String adminLogin() {
        return PROPS.getProperty("admin.login");
    }

    public static String adminPassword() {
        return PROPS.getProperty("admin.password");
    }

    public static String initialProductName() {
        return PROPS.getProperty("initial.product.name");
    }

    public static double initialProductPrice() {
        return Double.parseDouble(PROPS.getProperty("initial.product.price"));
    }

    // Печать всех параметров перед запуском теста, кроме логина и пароля
    public static void printConfig() {
        System.out.println("=== Конфигурация запуска ===");
        System.out.println("URL стенда: " + standUrl());
        System.out.println("URL API: " + apiUrl());
        System.out.println("Таймаут поиска элементов: " + elementTimeoutMs() + " мс");
        System.out.println("Режим логирования: " + loggingMode());
        System.out.println("Стартовый товар: " + initialProductName() + ", цена: " + initialProductPrice());
        System.out.println("============================");
    }
}
