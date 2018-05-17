package ru.patsiorin.otus.myunit;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.*;

/**
 * A Simple testing framework
 */
public class MyUnit {
    private static final Class<Test> ANNOTATION_TEST = Test.class;
    private static final Class<Before> ANNOTATION_BEFORE = Before.class;
    private static final Class<After> ANNOTATION_AFTER = After.class;

    private Class<?> testClass;
    private List<Method> beforeMethods = new ArrayList<>();
    private List<Method> testMethods = new ArrayList<>();
    private List<Method> afterMethods = new ArrayList<>();

    private MyUnit(Class<?> testClass) {
        this.testClass = testClass;
    }

    public static void main(String[] args) {
        if (args.length < 1) throw new IllegalArgumentException("Provide test class or package name");
        String testClassOrPackage = args[0];
        if (testClassOrPackage.charAt(testClassOrPackage.length() - 1) == '*') {
            runPackageTests(testClassOrPackage.substring(0, testClassOrPackage.length() - 2));
        } else {
            Result result = run(testClassOrPackage);
            System.out.println(result.getResultString());
        }
    }

    private static void runPackageTests(String packageName) {
        // we assume that in case the user wants to test the whole package
        // he uses the following argument format: com.package.name.*
        Set<Class<?>> classes = getClassesInPackage(packageName);
        for (Class cla : classes) {
            Result result = run(cla);
            System.out.println(result.getResultString());
        }
    }

    // https://stackoverflow.com/a/9571146
    private static Set<Class<?>> getClassesInPackage(String packageName) {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));
        return reflections.getSubTypesOf(Object.class);
    }

    /**
     * <p>
     *     Runs all tests in the provided class
     * </p>
     *
     * @param className Full qualified name of the class
     * @return Result
     * @throws RuntimeException
     */
    public static Result run(String className) {
        try {
            return run(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class " + className + "not found");
        }
    }

    /**<p>
     *   Runs all tests in the provided class
     * </p>
     *
     * @param cla
     * @return Result
     * @throws RuntimeException
     */
    public static Result run(Class<?> cla) {
        System.out.println("Running tests for " + cla.getName());
        MyUnit myUnit = new MyUnit(cla);
        myUnit.findFrameworkMethods();

        try {
            return myUnit.runTests();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Result runTests() throws Exception {
        Result result = new Result(testMethods.size());

        for (Method testMethod : testMethods) {
            Object testObject = getDefaultConstructor().newInstance();
            for (Method beforeMethod : beforeMethods) {
                beforeMethod.invoke(testObject);
            }

            char testResultChar;
            try {
                testMethod.invoke(testObject);
                testResultChar = '.';
            } catch (Exception e) {
                if (e.getCause() instanceof AssertionError) {
                    result.addFailure(new Failure(testMethod, testObject.getClass(), (AssertionError) e.getCause()));
                    testResultChar = 'E';
                } else {
                    // something bad has happened, not an AssertionError
                    throw new Exception(e);
                }
            }

            System.out.print(testResultChar);

            for (Method afterMethod : afterMethods) {
                afterMethod.invoke(testObject);
            }
        }
        System.out.println();
        result.setEndInstant(Instant.now());
        return result;
    }

    private void findFrameworkMethods() {
        for (Method method : testClass.getDeclaredMethods()) {
            final boolean isMethodPublic = (method.getModifiers() == Modifier.PUBLIC);
            final boolean hasMethodParams = method.getParameters().length != 0;
            final String methodName = method.getName();

            for (Annotation directAnnotation : method.getDeclaredAnnotations()) {
                Class<? extends Annotation> annotationClass = directAnnotation.annotationType();
                try {
                    if (Objects.equals(annotationClass, ANNOTATION_BEFORE)) {
                        addMethod(beforeMethods, method, isMethodPublic, hasMethodParams, methodName);
                    }

                    if (Objects.equals(annotationClass, ANNOTATION_TEST)) {
                        addMethod(testMethods, method, isMethodPublic, hasMethodParams, methodName);
                    }

                    if (Objects.equals(annotationClass, ANNOTATION_AFTER)) {
                        addMethod(afterMethods, method, isMethodPublic, hasMethodParams, methodName);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private static void addMethod(List<Method> methodList, Method method, boolean isMethodPublic, boolean hasMethodParams, String methodName) throws Exception {
        checkModifier(isMethodPublic, methodName);
        checkParams(hasMethodParams, methodName);
        methodList.add(method);
    }

    private Constructor getDefaultConstructor() throws Exception {
        Constructor defaultConstructor;
        try {
            defaultConstructor = testClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new Exception("Test class should have a no-args constructor");
        }
        return defaultConstructor;
    }

    private static void checkParams(boolean hasMethodParams, String methodName) throws Exception {
        if (hasMethodParams) {
            throw new Exception(String.format("Method %s should have not parameters", methodName));
        }
    }

    private static void checkModifier(boolean isMethodPublic, String methodName) throws Exception {
        if (!isMethodPublic) {
            throw new Exception(String.format("Method %s should be public", methodName));
        }
    }


}
