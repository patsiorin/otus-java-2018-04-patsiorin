package ru.patsiorin.otus.myunit;

import java.lang.reflect.Method;

public class Failure {
    private Method method;
    private Class<?> testClass;
    private AssertionError error;

    public Failure(Method method, Class<?> testClass, AssertionError error) {
        this.method = method;
        this.testClass = testClass;
        this.error = error;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public AssertionError getError() {
        return error;
    }
}
