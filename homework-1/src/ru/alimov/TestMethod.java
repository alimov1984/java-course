package ru.alimov;

import java.lang.reflect.Method;

public class TestMethod implements Comparable<TestMethod> {
    private Method method;
    private int priority;
    private Object[] source;
    private Class[] parameterTypes;

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setSource(Object[] source) {
        this.source = source;
    }

    public Method getMethod() {
        return method;
    }

    public int getPriority() {
        return priority;
    }

    public Object[] getSource() {
        return source;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public int compareTo(TestMethod otherMethod) {
        if (this.priority > otherMethod.priority)
            return 1;
        else if (this.priority < otherMethod.priority)
            return -1;
        else
            return 0;
    }
}
