package org.example;

import java.util.ArrayList;
import java.util.List;

public class HomeworkOne {
    public static boolean isEven(int n) {
        return n % 2 == 0;
    }

    public static String checkAccess(int age) {
        return age > 18 ? "Allowed" : "Denied";
    }

    public static boolean isPositive(int n) {
        return n >= 0 ? true : false;
    }

    public static String getGrade(int score) {
        if (score >= 0 && score <= 20) {
            return "E";
        } else if (score >= 21 && score <= 40) {
            return "D";
        } else if (score >= 41 && score <= 60) {
            return "C";
        } else if (score >= 61 && score <= 80) {
            return "B";
        } else if (score >= 81 && score <= 100) {
            return "A";
        } else {
            return "Error";
        }
    }

    public static String blastOff(int start) {
        StringBuilder result = new StringBuilder();

        for (int i = start; i >= 1; i--) {
            result.append(i).append(" ");
        }

        result.append("Поехали!");
        return result.toString();
    }

    public static int sumToN(int n) {
        int sum = 0;

        for (int i = 1; i <= n; i++) {
            sum += i;
        }

        return sum;
    }

    public static boolean hasBug(String[] messages) {
        for (String message : messages) {
            if (message.equalsIgnoreCase("Bug")) {
                return true;
            }
        }

        return false;
    }

    public static String getEvenInRange(int start, int end) {
        String result = "";

        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (!result.isEmpty()) {
                    result += " ";
                }
                result += i;
            }
        }

        return result;
    }

    public static int findMax(int[] arr) {
        int max = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }

        return max;
    }

    public static String[] reverse(String[] arr) {
        String[] result = new String[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[arr.length - 1 - i];
        }

        return result;
    }

    public static double calcAverage(List<Integer> list) {
        int sum = 0;

        for (int number : list) {
            sum += number;
        }

        return (double) sum / list.size();
    }

    public static List<String> removeSpecificName(
            List<String> list, String nameToRemove) {

        List<String> result = new ArrayList<>();

        for (String name : list) {
            if (!name.equals(nameToRemove)) {
                result.add(name);
            }
        }

        return result;
    }
}
