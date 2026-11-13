import org.example.HomeworkOne;
import org.example.Methods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThirdHomeworkAssertTests {

    @BeforeEach
    void printStart() {
        Methods.printStart();
    }

    @AfterEach
    void printEnd() {
        Methods.printEnd();
    }

    @Test
    void isEvenAssertTest() {
        int n = 8;
        boolean actual = HomeworkOne.isEven(n);
        assertTrue(actual,
                "Ожидалось: число " + n + " четное (true), получено: " + actual);
    }

    @Test
    void getGradeAssertTest() {
        int score = 75;
        String expected = "B";
        String actual = HomeworkOne.getGrade(score);
        assertEquals(expected, actual,
                "Ожидалась оценка " + expected + " за " + score + " баллов, получено: " + actual);
    }

    @Test
    void removeSpecificNameAssertTest() {
        List<String> names = new ArrayList<>(Arrays.asList("Миша", "Вася", "Петя", "Коля"));
        List<String> expected = Arrays.asList("Вася", "Петя", "Коля");
        List<String> actual = HomeworkOne.removeSpecificName(names, "Миша");
        assertEquals(expected, actual,
                "Ожидался список " + expected + ", получен: " + actual);
    }

    @Test
    void sumToNWrongExpectationAssertTest() {
        int n = 5;
        int expected = 100; // заведомо неверное ожидание — ассерт должен упасть
        int actual = HomeworkOne.sumToN(n);
        assertEquals(expected, actual,
                "Ожидалась сумма " + expected + " для n = " + n + ", получено: " + actual);
    }
}
