package org.example;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ComparatorAndStreamHomework {

    // Задача 1: сортировка чисел по модулю в порядке убывания
    public static final Comparator<Integer> ABSOLUTE_DESCENDING_COMPARATOR =
            Comparator.comparingLong((Integer number) -> Math.abs((long) number)).reversed();

    // Задача 2: строки с sudo располагаются выше остальных
    public static final Comparator<String> SUDO_FIRST_COMPARATOR = (first, second) -> {
        boolean firstContainsSudo = first.contains("sudo");
        boolean secondContainsSudo = second.contains("sudo");

        if (firstContainsSudo && secondContainsSudo) {
            return 0;
        }
        if (firstContainsSudo) {
            return -1;
        }
        if (secondContainsSudo) {
            return 1;
        }
        return first.compareTo(second);
    };

    // Задача 3: подсчёт элементов коллекции, которые больше 5
    public static long countGreaterThanFive(Collection<Integer> numbers) {
        return numbers.stream()
                .filter(number -> number > 5)
                .count();
    }

    // Задача 4: преобразование только строк, состоящих из цифр, в массив Integer
    public static Integer[] convertNumericStringsToIntegers(String[] values) {
        return Arrays.stream(values)
                .filter(value -> value != null && value.matches("[0-9]+"))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }

    public static void main(String[] args) {
        Integer[] numbers = {-900, 783, 650, -500, -400, 321};
        Arrays.sort(numbers, ABSOLUTE_DESCENDING_COMPARATOR);
        System.out.println("Задача 1: " + Arrays.toString(numbers));

        String[] commands = {"ls -la", "sudo apt update", "pwd", "sudo systemctl restart ssh"};
        Arrays.sort(commands, SUDO_FIRST_COMPARATOR);
        System.out.println("Задача 2: " + Arrays.toString(commands));

        List<Integer> values = List.of(1, 5, 6, 10, -3);
        System.out.println("Задача 3: " + countGreaterThanFive(values));

        String[] strings = {"12", "abc", "7", "3.14", "-5", "0"};
        System.out.println("Задача 4: " + Arrays.toString(convertNumericStringsToIntegers(strings)));
    }
}
