package ru.patsiorin.otus;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MyHashMapEmptyTest {
    private static final String TEST_VALUE = "value";
    private static final String TEST_KEY = "key";

    private  Map<String, String> map;

    public static final int STRING_LENGTH = 10;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String randomString() {
        char[] buff = new char[STRING_LENGTH];
        for (int i = 0; i < buff.length; i++) {
            buff[i] = ALPHABET.charAt((int)(Math.random()*ALPHABET.length()));
        }
        return new String(buff);
    }

    @Before
    public void setUp() {
        map = new MyHashMap<>(16);
    }

    @Test (expected = NullPointerException.class)
    public void testPutNullKey() {
        map.put(null, TEST_VALUE);
    }

    @Test
    public void testPutNullValue() {
        map.put("null", null);
        assertEquals(1, map.size());
        assertNull(map.get("null"));
    }

    @Test
    public void testPutOne() {
        String ret = map.put(TEST_KEY, TEST_VALUE);

        assertEquals(1, map.size());
        assertEquals(TEST_VALUE, map.get(TEST_KEY));
        assertNull(ret);
    }

    @Test
    public void testPutDuplicate() {
        String oldValue = "value1";
        String newValue = "value2";
        map.put(TEST_KEY, oldValue);
        String ret = map.put(TEST_KEY, newValue);

        assertEquals(1, map.size());
        assertEquals(oldValue, ret);
        assertEquals(newValue, map.get(TEST_KEY));
    }

    @Test
    public void testPutMultiple() {
        int n = 100;
        String[] keys = new String[n];

        for (int i = 0; i < n; i++) {
            keys[i] = "key" + i;
            map.put(keys[i], "value" + i);
        }

        assertEquals("sizes should be equal", n, map.size());
        map.put("more", "more");
        assertEquals(n + 1, map.size());
        for (int i = 0; i < n; i++) {
            assertEquals("value" + i, map.get(keys[i]));
        }
    }

    @Test
    public void testRemoveSingle() {
        String key = "key";
        String value = "value";

        map.put(key, value);
        String ret = map.remove(key);
        assertEquals(value, ret);
        assertEquals(0, map.size());
    }

    @Test
    public void testRemoveNoMapping() {
        assertNull(map.remove("not in the map"));
    }

    @Test
    public void testGetRemovedValue() {
        String key = "key";
        map.put(key, "some value");
        map.remove(key);
        assertNull(map.get(key));

    }

    @Test
    public void testPutRandomKeyValues() {
        int size = 100_000;
        String[] keys = new String[size];
        String[] values = new String[size];
        for (int i = 0; i < size; i++) {
            keys[i] = randomString();
            values[i] = randomString();
            map.put(keys[i], values[i]);
        }

        assertEquals(size, map.size());

        for (int i = 0; i < size; i++) {
            assertEquals(values[i], map.get(keys[i]));
        }

        for (int i = 0; i < size; i += 2) {
            assertEquals(values[i], map.remove(keys[i]));
            assertNull(map.remove(keys[i]));
        }

        assertEquals(size/2, map.size());

    }

    @Test
    public void testConstructMapFromOther() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Moscow", "Russia");
        hashMap.put("Beijing", "China");
        hashMap.put("Bangkok", "Thailand");
        hashMap.put("Kiev", "Ukraine");
        hashMap.put("Minsk", "Belarus");

        Map<String, String> myHashMap = new HashMap<>(hashMap);
        assertEquals(hashMap.size(), myHashMap.size());

        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            assertEquals(entry.getValue(), myHashMap.get(entry.getKey()));
        }

    }
}
