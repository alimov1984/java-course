package ru.alimov;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.alimov.TestAnnotations.Test;
import static ru.alimov.TestAnnotations.BeforeSuite;
import static ru.alimov.TestAnnotations.AfterSuite;
import static ru.alimov.TestAnnotations.CsvSource;
import static ru.alimov.TestAnnotations.BeforeTest;
import static ru.alimov.TestAnnotations.AfterTest;
public class TestRunner {
    private static Map<String, Method> mainTestFunctions = new HashMap<>();
    private static List<TestMethod> testMethodList = new ArrayList<>();

    public static void main(String[] args) {
        runTests(new DealService());
    }

    public static void runTests(DealService dealService) {
        Class dealServiceClass = dealService.getClass();
        for (Method method : dealServiceClass.getDeclaredMethods()) {
            prepareBeforeSuiteAnnotation(method);
            prepareAfterSuiteAnnotation(method);
            prepareBeforeTestAnnotation(method);
            prepareAfterTestAnnotation(method);
            prepareTestAnnotation(method);
        }
        executeTests(dealService);
    }

    private static void prepareBeforeSuiteAnnotation(Method method) {
        if (!method.isAnnotationPresent(BeforeSuite.class)) {
            return;
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            System.out.printf("ERROR @BeforeSuite annotation! %s is not static function", method.getName());
            return;
        }
        if (method.getParameterCount() > 0) {
            System.out.printf("ERROR @BeforeSuite annotation! %s has parameters", method.getName());
            return;
        }
        if (mainTestFunctions.get(BeforeSuite.class.getSimpleName()) != null) {
            System.out.println("ERROR @BeforeSuite annotation! Annotation is used more one time.");
            return;
        }
        method.setAccessible(true);
        mainTestFunctions.put(BeforeSuite.class.getSimpleName(), method);
    }


    private static void prepareAfterSuiteAnnotation(Method method) {
        if (!method.isAnnotationPresent(AfterSuite.class)) {
            return;
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            System.out.printf("ERROR @AfterSuite annotation!  %s is not static function.", method.getName());
            return;
        }
        if (method.getParameterCount() > 0) {
            System.out.printf("ERROR @AfterSuite annotation! %s has parameters", method.getName());
            return;
        }
        if (mainTestFunctions.get(AfterSuite.class.getSimpleName()) != null) {
            System.out.println("ERROR! AfterSuite annotation is used more one time.");
            return;
        }
        method.setAccessible(true);
        mainTestFunctions.put(AfterSuite.class.getSimpleName(), method);

    }

    private static void prepareBeforeTestAnnotation(Method method) {
        if (!method.isAnnotationPresent(BeforeTest.class)) {
            return;
        }
        if (method.getParameterCount() > 0) {
            System.out.printf("ERROR @BeforeTest annotation! %s has parameters", method.getName());
            return;
        }
        if (mainTestFunctions.get(BeforeTest.class.getSimpleName()) != null) {
            System.out.println("ERROR @BeforeTest annotation! Annotation is used more one time.");
            return;
        }
        method.setAccessible(true);
        mainTestFunctions.put(BeforeTest.class.getSimpleName(), method);
    }


    private static void prepareAfterTestAnnotation(Method method) {
        if (!method.isAnnotationPresent(AfterTest.class)) {
            return;
        }
        if (method.getParameterCount() > 0) {
            System.out.printf("ERROR @AfterTest annotation! %s has parameters", method.getName());
            return;
        }
        if (mainTestFunctions.get(AfterTest.class.getSimpleName()) != null) {
            System.out.println("ERROR AfterTest annotation! Annotation is used more one time.");
            return;
        }
        method.setAccessible(true);
        mainTestFunctions.put(AfterTest.class.getSimpleName(), method);
    }

    private static void prepareTestAnnotation(Method method) {
        if (!method.isAnnotationPresent(Test.class)) {
            return;
        }
        if (Modifier.isStatic(method.getModifiers())) {
            System.out.printf("ERROR @Test annotation! %s is  not static function.", method.getName());
            return;
        }
        method.setAccessible(true);
        Test testAnnotation = method.getAnnotation(Test.class);

        TestMethod testMethod = new TestMethod();
        testMethod.setMethod(method);
        testMethod.setPriority(testAnnotation.priority());
        testMethod.setParameterTypes(method.getParameterTypes());
        if (method.isAnnotationPresent(CsvSource.class)) {
            CsvSource sourceAnnotation = method.getAnnotation(CsvSource.class);
            if (sourceAnnotation.source() != null && !sourceAnnotation.source().isEmpty()) {
                String[] sourceInitArray = sourceAnnotation.source().split(",");
                if (sourceInitArray.length != method.getParameterCount()) {
                    System.out.printf("ERROR @CsvSource annotation! Count of parameters in %s differs from defined in annotation.",
                            method.getName());
                    return;
                }
                Object[] sourceResultArray = new Object[sourceInitArray.length];
                Class[] paramClassArray = testMethod.getParameterTypes();
                for (int i = 0; i < paramClassArray.length; ++i) {
                    Object valueConverted = null;
                    if (paramClassArray[i].equals(String.class)) {
                        valueConverted = sourceInitArray[i].trim();
                    } else if (paramClassArray[i].equals(Boolean.class) || paramClassArray[i].equals(boolean.class)) {
                        valueConverted = Boolean.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Integer.class) || paramClassArray[i].equals(int.class)) {
                        valueConverted = Long.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Long.class) || paramClassArray[i].equals(long.class)) {
                        valueConverted = Long.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Float.class) || paramClassArray[i].equals(float.class)) {
                        valueConverted = Float.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Double.class) || paramClassArray[i].equals(double.class)) {
                        valueConverted = Double.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Byte.class) || paramClassArray[i].equals(byte.class)) {
                        valueConverted = Byte.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Short.class) || paramClassArray[i].equals(short.class)) {
                        valueConverted = Byte.valueOf(sourceInitArray[i].trim());
                    } else {
                        continue;
                    }
                    sourceResultArray[i] = valueConverted;
                }
                testMethod.setSource(sourceResultArray);
            }
        }
        testMethodList.add(testMethod);
    }

    private static void executeTests(DealService dealService) {
        //Execute something before executing all functions with @Test annotation
        Method beforeSuiteMethod = mainTestFunctions.get(BeforeSuite.class.getSimpleName());
        if (beforeSuiteMethod != null) {
            try {
                beforeSuiteMethod.invoke(null);
            } catch (Exception ex) {
                System.out.printf("ERROR @BeforeSuite annotation! While executing %s", beforeSuiteMethod.getName());
                return;
            }
        }

        List<TestMethod> methodForExecute = testMethodList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for (TestMethod testMethod : methodForExecute) {
            //Execute something before executing function with @Test annotation
            Method beforeMethod = mainTestFunctions.get(BeforeTest.class.getSimpleName());
            try {
                if (beforeMethod != null) {
                    if (Modifier.isStatic(beforeMethod.getModifiers())) {
                        beforeMethod.invoke(null);
                    } else {
                        beforeMethod.invoke(dealService);
                    }
                }
            } catch (Exception ex) {
                System.out.printf("ERROR @BeforeTest annotation! While executing %s", beforeMethod.getName());
                return;
            }

            try {
                //Execute function with @Test annotation
                testMethod.getMethod().invoke(dealService, testMethod.getSource());
            } catch (Exception ex) {
                System.out.printf("ERROR @Test annotation! While executing %s", testMethod.getMethod().getName());
                return;
            }
            //Execute something after executing function with @Test annotation
            Method afterMethod = mainTestFunctions.get(AfterTest.class.getSimpleName());
            try {
                if (afterMethod != null) {
                    if (Modifier.isStatic(afterMethod.getModifiers())) {
                        afterMethod.invoke(null);
                    } else {
                        afterMethod.invoke(dealService);
                    }
                }
            } catch (Exception ex) {
                System.out.printf("ERROR @AfterTest annotation! While executing %s", afterMethod.getName());
                return;
            }
        }
        //Execute something after executing all functions with @Test annotation
        Method afterSuiteMethod = mainTestFunctions.get(AfterSuite.class.getSimpleName());
        if (afterSuiteMethod != null) {
            try {
                afterSuiteMethod.invoke(null);
            } catch (Exception ex) {
                System.out.printf("ERROR AfterSuite annotation! While executing %s", afterSuiteMethod.getName());
            }
        }
    }

}
