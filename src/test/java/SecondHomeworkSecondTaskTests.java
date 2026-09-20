import org.example.HomeworkOne;
import org.example.Methods;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Stream;

class SecondHomeworkSecondTaskTests {

    @Test
    void isEvenTest() {
        int n = Methods.getRandomNumberWithBound(200) - 50;
        boolean expected = n % 2 == 0;
        boolean actual = HomeworkOne.isEven(n);
        Methods.report(actual == expected, "Четное ли число(" + n + ") → " + actual);
    }

    @Test
    void checkAccessTest() {
        int age = Methods.getRandomNumberWithBound(40);
        String expected = age > 18 ? "Allowed" : "Denied";
        String actual = HomeworkOne.checkAccess(age);
        Methods.report(expected.equals(actual), "Есть ли доступ (" + age + ") → " + actual);
    }

    @Test
    void isPositiveTest() {
        int n = Methods.getRandomNumberWithBound(200) - 100;
        boolean expected = n >= 0;
        boolean actual = HomeworkOne.isPositive(n);
        Methods.report(actual == expected, "Положительное ли число(" + n + ") → " + actual);
    }

    @Test
    void getGradeTest() {
        int score = Methods.getRandomNumberWithBound(120) - 10;
        String expected;
        if (score >= 0 && score <= 20) expected = "E";
        else if (score >= 21 && score <= 40) expected = "D";
        else if (score >= 41 && score <= 60) expected = "C";
        else if (score >= 61 && score <= 80) expected = "B";
        else if (score >= 81 && score <= 100) expected = "A";
        else expected = "Ошибка";

        String actual = HomeworkOne.getGrade(score);
        Methods.report(expected.equals(actual), "Резульат за баллы:(" + score + ") → " + actual);
    }

    @RepeatedTest(5)
    void blastOffTest() {
        int start = Methods.getRandomNumberWithBound(6) + 1;

        StringBuilder expected = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            expected.append(i).append(" ");
        }
        expected.append("Поехали!");

        String actual = HomeworkOne.blastOff(start);
        Methods.report(expected.toString().equals(actual),
                "Отсчет(" + start + ") → \"" + actual + "\"");
    }

    @RepeatedTest(4)
    void sumToNTest() {
        int n = Methods.getRandomNumberWithBound(20) + 1;
        int expected = n * (n + 1) / 2;
        int actual = HomeworkOne.sumToN(n);
        Methods.report(actual == expected, "Сумма чисел равна(" + n + ") → " + actual);
    }

    @RepeatedTest(3)
    void hasBugTest() {
        String[] messages = {
                "без бага",
                Methods.getRandomNumberWithBound(2) == 0 ? "Bug" : "и это без бага",
                "тож без база"
        };

        boolean expected = false;
        for (String msg : messages) {
            if (msg.equalsIgnoreCase("Bug")) {
                expected = true;
                break;
            }
        }

        boolean actual = HomeworkOne.hasBug(messages);
        Methods.report(actual == expected, "Найден баг - " + actual);
    }

    @RepeatedTest(4)
    void findMaxTest() {
        int size = Methods.getRandomNumberWithBound(6) + 3;
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = Methods.getRandomNumberWithBound(150) - 50;
        }

        int expected = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > expected) expected = arr[i];
        }

        int actual = HomeworkOne.findMax(arr);
        Methods.report(actual == expected, "Найти наибольшее число(" + Arrays.toString(arr) + ") → " + actual);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 0, -3})
    void paramGetEvenInRangeTest(int start) {
        int end = start + Methods.getRandomNumberWithBound(10) + 5;

        StringBuilder expected = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (!expected.isEmpty()) expected.append(" ");
                expected.append(i);
            }
        }

        String actual = HomeworkOne.getEvenInRange(start, end);
        Methods.report(expected.toString().equals(actual),
                "Четные числа из промежутка(" + start + ", " + end + ") → \"" + actual + "\"");
    }

    static Stream<Arguments> reverseProvider() {
        return Stream.of(
                Arguments.of((Object) new String[]{"й", "ц", "у"}),
                Arguments.of((Object) new String[]{"йцу"}),
                Arguments.of((Object) new String[]{}),
                Arguments.of((Object) new String[]{"я", "ч", "с", "м"})
        );
    }

    @ParameterizedTest
    @MethodSource("reverseProvider")
    void paramReverseTest(String[] original) {
        String[] actual = HomeworkOne.reverse(original);

        String[] expected = new String[original.length];
        for (int i = 0; i < original.length; i++) {
            expected[i] = original[original.length - 1 - i];
        }

        Methods.report(Arrays.equals(expected, actual),
                "Отобразить массив в обратном порядке(" + Arrays.toString(original) + ") → " + Arrays.toString(actual));
    }

    static Stream<List<Integer>> averageProvider() {
        return Stream.generate(() -> {
                    int size = Methods.getRandomNumberWithBound(6) + 1;
                    List<Integer> list = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        list.add(Methods.getRandomNumberWithBound(100));
                    }
                    return list;
                })
                .limit(5);
    }

    @ParameterizedTest
    @MethodSource("averageProvider")
    void paramCalcAverageTest(List<Integer> list) {
        if (list.isEmpty()) {
            Methods.report(true, "значение пустое - пропускаем");
            return;
        }

        double expected = 0;
        for (int num : list) expected += num;
        expected /= list.size();

        double actual = HomeworkOne.calcAverage(list);
        Methods.report(Math.abs(expected - actual) < 0.0001,
                "Посчитать среднее(" + list + ") → " + actual);
    }

    static Stream<List<String>> removeNameProvider() {
        return Stream.of(
                Arrays.asList("Миша", "Вася", "Петя", "Коля"),
                Arrays.asList("Петя", "Коля"),
                Arrays.asList("Вася"),
                new ArrayList<>()
        );
    }

    @ParameterizedTest
    @MethodSource("removeNameProvider")
    void paramRemoveSpecificNameTest(List<String> names) {
        String nameToRemove = "Миша";

        List<String> expected = new ArrayList<>();
        for (String n : names) {
            if (!n.equals(nameToRemove)) {
                expected.add(n);
            }
        }

        List<String> actual = HomeworkOne.removeSpecificName(new ArrayList<>(names), nameToRemove);
        Methods.report(expected.equals(actual),
                "Убрать из массива имя(" + names + ", \"" + nameToRemove + "\") → " + actual);
    }
}