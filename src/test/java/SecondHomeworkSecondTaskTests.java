import org.example.HomeworkOne;
import org.example.Methods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Tag("second_task")
class SecondHomeworkSecondTaskTests {

    @BeforeEach
    void printStart() {
        Methods.printStart();
    }

    @AfterEach
    void printEnd() {
        Methods.printEnd();
    }

    @Tag("boolean")
    @RepeatedTest(10)
    void isEvenTest() {
        int n = Methods.getRandomNumberWithBound(200) - 50;
        boolean expected = n % 2 == 0;
        boolean actual = HomeworkOne.isEven(n);
        assertEquals(expected, actual,
                "Число " + n + ": ожидалось " + expected + ", получено " + actual);
    }

    @Tag("string")
    @RepeatedTest(10)
    void checkAccessTest() {
        int age = Methods.getRandomNumberWithBound(40);
        String expected = age > 18 ? "Allowed" : "Denied";
        String actual = HomeworkOne.checkAccess(age);
        assertEquals(expected, actual,
                "Возраст " + age + ": ожидался доступ " + expected + ", получен " + actual);
    }

    @Tag("boolean")
    @RepeatedTest(10)
    void isPositiveTest() {
        int n = Methods.getRandomNumberWithBound(200) - 100;
        boolean expected = n >= 0;
        boolean actual = HomeworkOne.isPositive(n);
        assertEquals(expected, actual,
                "Число " + n + ": ожидалось " + expected + ", получено " + actual);
    }

    @Tag("string")
    @Tag("fail")
    @RepeatedTest(10)
    void getGradeTest() {
        int score = Methods.getRandomNumberWithBound(120) - 10;
        String expected;
        if (score >= 0 && score <= 20) expected = "E";
        else if (score >= 21 && score <= 40) expected = "D";
        else if (score >= 41 && score <= 60) expected = "C";
        else if (score >= 61 && score <= 80) expected = "B";
        else if (score >= 81 && score <= 100) expected = "A";
        else expected = "Error";

        String actual = HomeworkOne.getGrade(score);
        assertEquals(expected, actual,
                "Баллы " + score + ": ожидалась оценка " + expected + ", получена " + actual);
    }

    @Tag("string")
    @RepeatedTest(10)
    void blastOffTest() {
        int start = Methods.getRandomNumberWithBound(6) + 1;

        StringBuilder expected = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            expected.append(i).append(" ");
        }
        expected.append("Поехали!");

        String actual = HomeworkOne.blastOff(start);
        assertEquals(expected.toString(), actual,
                "Отсчет от " + start + ": ожидалось \"" + expected + "\", получено \"" + actual + "\"");
    }

    @Tag("number")
    @RepeatedTest(10)
    void sumToNTest() {
        int n = Methods.getRandomNumberWithBound(20) + 1;
        int expected = n * (n + 1) / 2;
        int actual = HomeworkOne.sumToN(n);
        assertEquals(expected, actual,
                "Сумма до " + n + ": ожидалось " + expected + ", получено " + actual);
    }

    @Tag("boolean")
    @RepeatedTest(10)
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
        assertEquals(expected, actual,
                "Сообщения " + Arrays.toString(messages) + ": ожидалось " + expected + ", получено " + actual);
    }

    @Tag("number")
    @RepeatedTest(10)
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
        assertEquals(expected, actual,
                "Массив " + Arrays.toString(arr) + ": ожидался максимум " + expected + ", получен " + actual);
    }

    @Tag("string")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 0, -3, 7, -10, 42, 100})
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
        assertEquals(expected.toString(), actual,
                "Диапазон (" + start + ", " + end + "): ожидалось \"" + expected + "\", получено \"" + actual + "\"");
    }

    static Stream<Arguments> reverseProvider() {
        return Stream.of(
                Arguments.of((Object) new String[]{"й", "ц", "у"}),
                Arguments.of((Object) new String[]{"йцу"}),
                Arguments.of((Object) new String[]{}),
                Arguments.of((Object) new String[]{"я", "ч", "с", "м"}),
                Arguments.of((Object) new String[]{"а", "б"}),
                Arguments.of((Object) new String[]{"раз", "два", "три", "четыре", "пять"}),
                Arguments.of((Object) new String[]{"x"}),
                Arguments.of((Object) new String[]{"1", "2", "3"}),
                Arguments.of((Object) new String[]{"кот", "пёс"}),
                Arguments.of((Object) new String[]{"один"})
        );
    }

    @Tag("array")
    @ParameterizedTest
    @MethodSource("reverseProvider")
    void paramReverseTest(String[] original) {
        String[] actual = HomeworkOne.reverse(original);

        String[] expected = new String[original.length];
        for (int i = 0; i < original.length; i++) {
            expected[i] = original[original.length - 1 - i];
        }

        assertArrayEquals(expected, actual,
                "Массив " + Arrays.toString(original) + ": ожидался разворот "
                        + Arrays.toString(expected) + ", получен " + Arrays.toString(actual));
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
                .limit(10);
    }

    @Tag("number")
    @ParameterizedTest
    @MethodSource("averageProvider")
    void paramCalcAverageTest(List<Integer> list) {
        double expected = 0;
        for (int num : list) expected += num;
        expected /= list.size();

        double actual = HomeworkOne.calcAverage(list);
        assertEquals(expected, actual, 0.0001,
                "Список " + list + ": ожидалось среднее " + expected + ", получено " + actual);
    }

    static Stream<List<String>> removeNameProvider() {
        return Stream.of(
                Arrays.asList("Миша", "Вася", "Петя", "Коля"),
                Arrays.asList("Петя", "Коля"),
                Arrays.asList("Вася"),
                new ArrayList<>(),
                Arrays.asList("Миша"),
                Arrays.asList("Миша", "Миша", "Оля"),
                Arrays.asList("Оля", "Таня", "Миша", "Ира"),
                Arrays.asList("Саша", "Даша"),
                Arrays.asList("Миша", "Саша", "Миша"),
                Arrays.asList("Коля")
        );
    }

    @Tag("list")
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
        assertEquals(expected, actual,
                "Список " + names + ", убрать \"" + nameToRemove + "\": ожидался " + expected + ", получен " + actual);
    }
}
