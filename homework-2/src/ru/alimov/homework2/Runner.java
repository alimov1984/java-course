package ru.alimov.homework2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.out;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Runner {

    public static void main(String[] args) {
        out.println(String.format("Удаление из листа всех дубликатов: %s", removeDublicateInList(List.of(1, 6, 8, 1, 6))));

        out.println(String.format("3-е наибольшее число: %d", getThirdMaxNumber(Arrays.asList(5, 2, 10, 9, 4, 3, 10, 1, 13))));

        out.println(String.format("3-е наибольшее уникальное» число: %d", getThirdUniqueMaxNumber(Arrays.asList(5, 2, 10, 9, 4, 3, 10, 1, 13))));

        List<Employee> employeeList = new ArrayList<>();
        Employee emp1 = new Employee("Светаева Светлана Николаевна", (short) 25, "инженер");
        employeeList.add(emp1);
        Employee emp2 = new Employee("Петров Петр Петрович", (short) 34, "Team lead");
        employeeList.add(emp2);
        Employee emp3 = new Employee("Андреев Андрей Андреевич", (short) 28, "Инженер");
        employeeList.add(emp3);
        Employee emp4 = new Employee("Николаев Николай Николаевич", (short) 38, "Аналитик");
        employeeList.add(emp4);
        Employee emp5 = new Employee("Иванов Иван Иванович", (short) 30, "Инженер");
        employeeList.add(emp5);
        out.println(String.format("Список имен 3 самых старших сотрудников с должностью «Инженер»: %s",
                getThreeOldestEmployees(employeeList)));

        out.println(String.format("Средний возраст сотрудников с должностью «Инженер»: %f",
                getAverageAgeEmployees(employeeList)));

        out.println(String.format("Самое длинное слово в списке: %s", getLongestWord(List.of("стол", "баня", "программа", "дождь"))));

        out.println(String.format("Сколько раз каждое слово встречается во входной строке: %s",
                getCountWordOccurrence("стол баня программа стол программа стол")));

        out.println(String.format("Cтроки из списка в порядке увеличения длины слова(сохранен алфавитный порядок для слов одинаковой длины): %s",
                getSortedStringList(List.of("программа", "стол", "дождь", "баня"))));

        String[] wordArray = new String[]{"стол баня еда дождь город", "цветы Москва лучший метро глаза",
                "конь деревня дом лавочка шкаф", "молодость танцы свет движение скорость", "жизнь книга встреча отпуск море"};
        out.println(String.format("Самое длинное слово в массиве: %s",
                getLongestWordFromArray(wordArray)));

    }

    private static <T> List<T> removeDublicateInList(List<T> inputList) {
        return inputList.stream().distinct().collect(Collectors.toList());
    }

    private static Integer getThirdMaxNumber(List<Integer> inputList) {
        return inputList.stream().sorted(Comparator.reverseOrder()).skip(2).findFirst().orElseThrow(() -> new EmptyResultException("Переданная коллекция содержит меньше 3-х элементов"));
    }

    private static Integer getThirdUniqueMaxNumber(List<Integer> inputList) {
        return inputList.stream().distinct().sorted(Comparator.reverseOrder()).skip(2).findFirst().orElseThrow(() -> new EmptyResultException("Переданная коллекция содержит меньше 3-х элементов"));
    }

    private static List<String> getThreeOldestEmployees(List<Employee> inputList) {
        return inputList.stream().filter(emp -> "Инженер".equalsIgnoreCase(emp.getPosition())).sorted((emp1, emp2) -> emp2.getAge() - emp1.getAge()).limit(3).map(emp -> emp.getFio()).collect(Collectors.toList());
    }

    private static Double getAverageAgeEmployees(List<Employee> inputList) {
        return inputList.stream().filter(emp -> "Инженер".equalsIgnoreCase(emp.getPosition())).mapToInt(emp -> emp.getAge()).average().orElseThrow(() -> new EmptyResultException("Переданная коллекция не содержиит ни одного сотрудника"));
    }

    private static String getLongestWord(List<String> inputList) {
        return inputList.stream().max(Comparator.comparing(String::length)).orElseThrow(() -> new EmptyResultException("Переданная коллекция пуста"));
    }

    private static Map<String, Long> getCountWordOccurrence(String inputWord) {
        return Arrays.stream(inputWord.split(" ")).collect(groupingBy(w -> w, counting()));
    }

    private static List<String> getSortedStringList(List<String> inputList) {
        return inputList.stream().sorted(Comparator.comparing(String::length).thenComparing(w -> w)).collect(Collectors.toList());
    }

    private static String getLongestWordFromArray(String[] inputArray) {
        return Arrays.stream(inputArray).flatMap(row -> Arrays.stream(row.split(" "))).max(Comparator.comparing(String::length)).orElseThrow(() -> new EmptyResultException("Переданная коллекция пуста"));
    }


}
