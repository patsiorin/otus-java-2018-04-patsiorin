package ru.patsiorin.otus;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Object2JSONWriterTest {
    private Object2JSONWriter writer;
    private Gson gson;

    @Before
    public void setUp() {
        writer = new Object2JSONWriter();
        gson = new Gson();
    }

    @Test
    public void testPrimitiveBooleanToJson() {
        boolean i = true;
        String expected = gson.toJson(i);
        String result = writer.toJson(i);

        assertEquals(expected, result);
    }

    @Test
    public void testWrapperBooleanToJson() {
        Boolean i = Boolean.FALSE;
        String expected = gson.toJson(i);
        String result = writer.toJson(i);

        assertEquals(expected, result);
    }


    @Test
    public void testPrimitiveIntToJson() {
        int i = 777;
        String expected = gson.toJson(i);
        String result = writer.toJson(i);

        assertEquals(expected, result);
    }

    @Test
    public void testWrapperIntToJson() {
        Integer i = 777;
        String expected = gson.toJson(i);
        String result = writer.toJson(i);

        assertEquals(expected, result);
    }

    @Test
    public void testPrimitiveDoubleToJson() {
        double d = 777.0;
        String expected = gson.toJson(d);
        String result = writer.toJson(d);

        assertEquals(expected, result);
    }

    @Test
    public void testWrapperDoubleToJson() {
        Double d = 777.0;
        String expected = gson.toJson(d);
        String result = writer.toJson(d);

        assertEquals(expected, result);
    }

    @Test
    public void testBigIntegerToJson() {
        BigInteger value = BigInteger.valueOf(123456L);

        String expected = gson.toJson(value);
        String result = writer.toJson(value);

        assertEquals(expected, result);
    }

    @Test
    public void testBigDecimalToJson() {
        BigDecimal value = BigDecimal.valueOf(123456.3);

        String expected = gson.toJson(value);
        String result = writer.toJson(value);

        assertEquals(expected, result);
    }

    @Test
    public void testStringToJson() {
        String str = "this is a sting";
        String expected = gson.toJson(str);
        String result = writer.toJson(str);

        assertEquals(expected, result);
    }

    @Test
    public void testPrimitiveCharToJson() {
        char ch = 'c';
        String expected = gson.toJson(ch);
        String result = writer.toJson(ch);

        assertEquals(expected, result);
    }

    @Test
    public void testWrapperCharToJson() {
        Character ch = 'c';
        String expected = gson.toJson(ch);
        String result = writer.toJson(ch);

        assertEquals(expected, result);
    }

    @Test
    public void testCharArrayToJson() {
        char[] chars = new char[]{'a', 'b', 'c'};
        String expected = gson.toJson(chars);
        String result = writer.toJson(chars);

        assertEquals(expected, result);
    }

    @Test
    public void testStringArrayToJson() {
        String[] chars = new String[]{"a", "b", "c"};
        String expected = gson.toJson(chars);
        String result = writer.toJson(chars);

        assertEquals(expected, result);
    }


    @Test
    public void testEmptyObjectToJson() {
        Object ob = new Object();
        String expected = gson.toJson(ob);
        String result = writer.toJson(ob);

        assertEquals(expected, result);
    }

    @Test
    public void testEmptyObjectArrayToJson() {
        Object[] objects = new Object[0];
        String expected = gson.toJson(objects);
        String result = writer.toJson(objects);

        assertEquals(expected, result);
    }

    @Test
    public void testEmptyPrimitiveArrayToJson() {
        int[] ints = new int[0];
        String expected = gson.toJson(ints);
        String result = writer.toJson(ints);

        assertEquals(expected, result);
    }

    @Test
    public void testPrimitiveArrayToJson() {
        int[] ints = new int[]{1, 2, 3, 4};
        String expected = gson.toJson(ints);
        String result = writer.toJson(ints);

        assertEquals(expected, result);
    }

    @Test
    public void testWrapperArrayToJson() {
        Integer[] ints = new Integer[]{1, 2, 3, 4};
        String expected = gson.toJson(ints);
        String result = writer.toJson(ints);

        assertEquals(expected, result);
    }

    @Test
    public void testPrimitiveBooleanArrayToJson() {
        boolean[] values = new boolean[]{true, false, true, false};
        String expected = gson.toJson(values);
        String result = writer.toJson(values);

        assertEquals(expected, result);
    }

    @Test
    public void testWrapperBooleanArrayToJson() {
        Boolean[] values = new Boolean[]{true, false, true, false};
        String expected = gson.toJson(values);
        String result = writer.toJson(values);

        assertEquals(expected, result);
    }

    @Test
    public void testMapOfNumberToJson() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        String expected = gson.toJson(map);
        String result = writer.toJson(map);

        assertEquals(expected, result);
    }

    @Test
    public void testBagOfPrimitivesToJson() {
        ComplexObject bag = new ComplexObject();
        String expected = gson.toJson(bag);
        String result = writer.toJson(bag);

        assertEquals(expected, result);
    }

    @Test
    public void testParametrizedClassToJson() {
        ParametrizedClass<Integer> c = new ParametrizedClass<>(33);
        String expected = gson.toJson(c);
        String result = writer.toJson(c);

        assertEquals(expected, result);
    }

    @Test
    public void testNullReferenceToJson() {
        String s = null;
        String result = writer.toJson(s);

        assertNull(result);
    }

    @Test
    public void testArrayWithNullsToJson() {
        String[] strs = new String[]{null, null, null, "hello"};

        String expected = gson.toJson(strs);
        String result = writer.toJson(strs);
        assertEquals(expected, result);
    }

    @Test
    public void testListWithNullsToJson() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add(null);
        list.add("B");
        String expected = gson.toJson(list);
        String result = writer.toJson(list);
    }

    @Test
    public void testMapWithNullsToJson() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", null);
        map.put("three", 3);

        String expected = gson.toJson(map);
        String result = writer.toJson(map);

        assertEquals(expected, result);
    }

    static class ComplexObject {
//        Class<?> cl = Object2JSONWriter.class;
        Integer intObject = 5;
        List<String> list = new ArrayList<>();
        {
            list.add("Hey");
            list.add("mraz");
        }
        Map<List, Integer> map = new HashMap<>();
        {
            List<Integer> l1 = new ArrayList<>();
            l1.add(1); l1.add(2);
            List<Integer> l2 = new ArrayList<>();
            l2.add(1); l2.add(2);
            map.put(l1, 1);
            map.put(l2, 4);
        }
        Object2JSONWriter wr = new Object2JSONWriter();
        static long l = 12;
        private char ch = 'k';
        public byte byteValue = 1;
        private int value1 = 1;
        private String value2 = "abc";
        private transient int value3 = 3;
    }

    static class ParametrizedClass<T> {
        T t;
        T p;

        public ParametrizedClass(T t) {
            this.t = t;
        }
    }
}
