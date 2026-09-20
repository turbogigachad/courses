package org.example;

import java.util.stream.Stream;
import java.util.Random;
import java.util.stream.IntStream;

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
}
