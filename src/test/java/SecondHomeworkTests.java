import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.example.HomeworkOne;
import org.example.Methods;


class SecondHomeworkTests {

    @BeforeEach
    void printStart() {
        Methods.printStart();
    }

    @AfterEach
    void printEnd() {
        Methods.printEnd();
    }

    @Test
    void runIsEvenWithRandomNumberOnceTest() {
        int n = Methods.getRandomNumberWithBound(100)+1;
        System.out.println(n + " четное? Ответ: " + HomeworkOne.isEven(n));
    }

    @Test
    void runCheckAccessTwentyTimesTest() {
        for (int i = 0; i < 20; i++) {
            int age = Methods.getRandomNumberWithBound(100);
            System.out.println("Возраст = " + age + ". Доступ = " + HomeworkOne.checkAccess(age));
        }
    }

    @ParameterizedTest
    @MethodSource("org.example.Methods#randomScores")
    void runGetGradeParameterizedTest(int score) {
        System.out.println("Результат(" + score + ") = " + HomeworkOne.getGrade(score));
    }
}