package ru.patsiorin.otus.objectmemory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Simple reflection-based recursive object-traversal algorithm
 * for printing out an object's usage of memory in Java.
 *
 * Assumptions were made:
 *  - Each object uses 8 bytes of memory for its header;
 *  - Each object is padded to be divisible by 8;
 *  - Object reference fields use 4 bytes of memory
 *  - Arrays take 4 bytes to store their length.
 *
 *  The algorithm does not take into account memory usage of the parents of an object.
 *
 * @author Valerii Patsiorin
 *
 */
public class SizeOf {
    public static void main(String[] args) throws IllegalAccessException {
        printObjectMemoryUsage(new Object(), "new Object()");
        printObjectMemoryUsage(new String(), "new String()");
        printObjectMemoryUsage(new String("1"), "new String(\"1\")");
        printObjectMemoryUsage(new String("11"), "new String(\"11\")");
        printObjectMemoryUsage(new String("111"), "new String(\"111\")");
        printObjectMemoryUsage(new int[]{}, "new int[]{}");
        printObjectMemoryUsage(new int[]{1}, "new int[]{1}");
        printObjectMemoryUsage(new int[]{1, 2}, "new int[]{1, 2}");
        printObjectMemoryUsage(new String[]{"a"}, "new String[]{\"a\"}");
        printObjectMemoryUsage(new String[]{"a", "b"}, "new String[]{\"a\", \"b\"}");
        printObjectMemoryUsage(new String[]{"pool", "pool", "pool"}, "new String[]{\"pool\", \"pool\", \"pool\"}");

    }

    static void printObjectMemoryUsage(Object object, String message) throws IllegalAccessException {
        // use a hashset to check for duplicate objects
        Set<Integer> identityHashCodes = new HashSet<>();
        StringBuilder report = new StringBuilder();
        report.append(String.format("%s size:\n", message));
        int result = calculateObjectSizeWithoutDuplicates(object, identityHashCodes, report, 0);
        report.append(String.format("=%db\n\n", result));
        System.out.println(report);
    }

    private static int calculateObjectSizeWithoutDuplicates(Object object, Set<Integer> identityHasCodes, StringBuilder report, int level) throws IllegalAccessException {
        String tabs = tabs(level++);
        if (object == null) {
            report.append(tabs).append("+0b (reference is null)\n");
            return 0;
        }

        if (identityHasCodes.contains(System.identityHashCode(object))) {
            report.append(tabs).append("+0b (duplicate object)\n");
            return 0;
        } else {
            identityHasCodes.add(System.identityHashCode(object));
        }

        int size = ElementSizeInBytes.OBJ_HEADER.getSize();
        report.append(tabs).append(String.format("+%db (header)\n", size));

        if (object.getClass().isArray()) {
            size += getSizeOfArray(object, identityHasCodes, report, level);
        } else {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if ((field.getModifiers() & Modifier.STATIC) == 0) {
                    size += getFieldSize(object, identityHasCodes, report, level, field);
                }
            }
        }
        return getPaddedSize(size, report, level);
    }

    private static int getFieldSize(Object object, Set<Integer> identityHasCodes, StringBuilder report, int level, Field field) throws IllegalAccessException {
        String tabs = tabs(level);
        int fieldSize;
        String fieldName = field.getName();
        String typeName = field.getType().getSimpleName();

        if (field.getType().isPrimitive()) {
            fieldSize = ElementSizeInBytes.forPrimitiveField(field.getType().getName());
            report.append(tabs).append(String.format("+%db (primitive [name=%s,type=%s])\n", fieldSize, fieldName, typeName));
        } else {
            fieldSize = ElementSizeInBytes.OBJ_REF.getSize();
            report.append(tabs).append(String.format("+%db (reference [name=%s, type=%s]) ->\n", fieldSize, fieldName, typeName));
            fieldSize += calculateObjectSizeWithoutDuplicates(field.get(object), identityHasCodes, report, level);
        }
        return fieldSize;
    }

    private static String tabs(int level) {
        return new String(new char[level]).replace('\0', '\t');
    }


    private static int getSizeOfArray(Object object, Set<Integer> identityHasCodes, StringBuilder report, int level) throws IllegalAccessException {
        String tabs = tabs(level);
        int size = ElementSizeInBytes.ARRAY_SIZE.getSize();
        report.append(tabs).append(String.format("+%db (bytes to store array length)\n", size));

        int arrayLength = Array.getLength(object);
        if (object.getClass().getComponentType().isPrimitive()) {
            String primitiveName = object.getClass().getComponentType().getName();
            int primitiveSize = ElementSizeInBytes.forPrimitiveField(primitiveName);
            int arraySize = arrayLength * primitiveSize;
            report.append(tabs)
                    .append(String.format("+%db (array of primtives length=%d*%s=%d)\n", arraySize, arrayLength, primitiveName, primitiveSize));
            size += arraySize;
        } else {
            size += arrayLength * ElementSizeInBytes.OBJ_REF.getSize();
            for (int i = 0; i < arrayLength; i++) {
                report.append(tabs).append(String.format("array[%d] ->\n", i));
                size += calculateObjectSizeWithoutDuplicates(Array.get(object, i), identityHasCodes, report, level);
            }
        }
        return size;
    }

    private static int getPaddedSize(int size, StringBuilder report, int level) {
        String tabs = tabs(level);
        int paddingSize = ElementSizeInBytes.PADDING.getSize();
        int padding = (size % paddingSize == 0 ? 0 : paddingSize - (size % paddingSize));
        report.append(tabs).append(String.format("+%db (padding)\n", padding));
        return size + padding;
    }

}

enum ElementSizeInBytes {
    OBJ_HEADER(8), OBJ_REF(4),
    INT(4), FLOAT(4), DOUBLE(8), LONG(8), SHORT(2), CHAR(2), BOOLEAN(1), BYTE(1),
    ARRAY_SIZE(4), PADDING(8);

    private int size;

    ElementSizeInBytes(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public static int forPrimitiveField(String primitiveName) {
        switch (primitiveName) {
            case "int":
                return INT.getSize();
            case "float":
                return FLOAT.getSize();
            case "double":
                return DOUBLE.getSize();
            case "long":
                return LONG.getSize();
            case "short":
                return SHORT.getSize();
            case "boolean":
                return BOOLEAN.getSize();
            case "char":
                return CHAR.getSize();
            case "byte":
                return BYTE.getSize();
            default:
                throw new IllegalArgumentException("no such primitive");
        }
    }
}
