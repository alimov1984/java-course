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
    private static Map<Class, Method> mainTestFunctions = new HashMap<>();
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
            throw new TestException(String.format("ERROR @BeforeSuite annotation! %s is not static function",
                    method.getName()));
        }
        if (method.getParameterCount() > 0) {
            throw new TestException(String.format("ERROR @BeforeSuite annotation! %s has parameters",
                    method.getName(), method.getName()));
        }
        if (mainTestFunctions.get(BeforeSuite.class.getSimpleName()) != null) {
            throw new TestException("ERROR @BeforeSuite annotation! Annotation is used more one time.");
        }
        method.setAccessible(true);
        mainTestFunctions.put(BeforeSuite.class, method);
    }


    private static void prepareAfterSuiteAnnotation(Method method) {
        if (!method.isAnnotationPresent(AfterSuite.class)) {
            return;
        }
        if (!Modifier.isStatic(method.getModifiers())) {
            throw new TestException(String.format("ERROR @AfterSuite annotation!  %s is not static function.",
                    method.getName()));
        }
        if (method.getParameterCount() > 0) {
            throw new TestException(String.format("ERROR @AfterSuite annotation! %s has parameters",
                    method.getName()));
        }
        if (mainTestFunctions.get(AfterSuite.class.getSimpleName()) != null) {
            throw new TestException("ERROR! AfterSuite annotation is used more one time.");
        }
        method.setAccessible(true);
        mainTestFunctions.put(AfterSuite.class, method);

    }

    private static void prepareBeforeTestAnnotation(Method method) {
        if (!method.isAnnotationPresent(BeforeTest.class)) {
            return;
        }
        if (method.getParameterCount() > 0) {
            throw new TestException(String.format("ERROR @BeforeTest annotation! %s has parameters",
                    method.getName()));
        }
        if (mainTestFunctions.get(BeforeTest.class.getSimpleName()) != null) {
            throw new TestException(String.format("ERROR @BeforeTest annotation! Annotation is used more one time."));
        }
        method.setAccessible(true);
        mainTestFunctions.put(BeforeTest.class, method);
    }


    private static void prepareAfterTestAnnotation(Method method) {
        if (!method.isAnnotationPresent(AfterTest.class)) {
            return;
        }
        if (method.getParameterCount() > 0) {
            throw new TestException(String.format("ERROR @AfterTest annotation! %s has parameters",
                    method.getName()));
        }
        if (mainTestFunctions.get(AfterTest.class.getSimpleName()) != null) {
            throw new TestException("ERROR AfterTest annotation! Annotation is used more one time.");
        }
        method.setAccessible(true);
        mainTestFunctions.put(AfterTest.class, method);
    }

    private static void prepareTestAnnotation(Method method) {
        if (!method.isAnnotationPresent(Test.class)) {
            return;
        }
        if (Modifier.isStatic(method.getModifiers())) {
            throw new TestException(String.format("ERROR @Test annotation! %s is  not static function.",
                    method.getName()));
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
                    if (Modifier.isStatic(method.getModifiers())) {
                        throw new TestException(String.format("ERROR @CsvSource annotation! Count of parameters in %s differs from defined in annotation.",
                                method.getName()));
                    }
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
                        valueConverted = Integer.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Long.class) || paramClassArray[i].equals(long.class)) {
                        valueConverted = Long.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Float.class) || paramClassArray[i].equals(float.class)) {
                        valueConverted = Float.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Double.class) || paramClassArray[i].equals(double.class)) {
                        valueConverted = Double.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Byte.class) || paramClassArray[i].equals(byte.class)) {
                        valueConverted = Byte.valueOf(sourceInitArray[i].trim());
                    } else if (paramClassArray[i].equals(Short.class) || paramClassArray[i].equals(short.class)) {
                        valueConverted = Short.valueOf(sourceInitArray[i].trim());
                    } else {
                        throw new TestException(String.format("ERROR @Test annotation! Not found handler for type %s in %s.",
                                paramClassArray[i].getSimpleName(), method.getName()));
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
        Method beforeSuiteMethod = mainTestFunctions.get(BeforeSuite.class);
        if (beforeSuiteMethod != null) {
            try {
                beforeSuiteMethod.invoke(null);
            } catch (Exception ex) {
                throw new TestException(String.format("ERROR @BeforeSuite annotation! While executing %s", beforeSuiteMethod.getName()), ex);
            }
        }

        List<TestMethod> methodForExecute = testMethodList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for (TestMethod testMethod : methodForExecute) {
            //Execute something before executing function with @Test annotation
            Method beforeMethod = mainTestFunctions.get(BeforeTest.class);
            try {
                if (beforeMethod != null) {
                    if (Modifier.isStatic(beforeMethod.getModifiers())) {
                        beforeMethod.invoke(null);
                    } else {
                        beforeMethod.invoke(dealService);
                    }
                }
            } catch (Exception ex) {
                throw new TestException(String.format("ERROR @BeforeTest annotation! While executing %s",
                        beforeMethod.getName()), ex);
            }

            try {
                //Execute function with @Test annotation
                testMethod.getMethod().invoke(dealService, testMethod.getSource());
            } catch (Exception ex) {
                throw new TestException(String.format("ERROR @Test annotation! While executing %s",
                        testMethod.getMethod().getName()), ex);
            }
            //Execute something after executing function with @Test annotation
            Method afterMethod = mainTestFunctions.get(AfterTest.class);
            try {
                if (afterMethod != null) {
                    if (Modifier.isStatic(afterMethod.getModifiers())) {
                        afterMethod.invoke(null);
                    } else {
                        afterMethod.invoke(dealService);
                    }
                }
            } catch (Exception ex) {
                throw new TestException(String.format("ERROR @AfterTest annotation! While executing %s",
                        afterMethod.getName()), ex);
            }
        }
        //Execute something after executing all functions with @Test annotation
        Method afterSuiteMethod = mainTestFunctions.get(AfterSuite.class);
        if (afterSuiteMethod != null) {
            try {
                afterSuiteMethod.invoke(null);
            } catch (Exception ex) {
                throw new TestException(String.format("ERROR AfterSuite annotation! While executing %s",
                        afterSuiteMethod.getName()), ex);
            }
        }
    }

}
