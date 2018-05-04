package ru.patsiorin.otus;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MyHshMapWithValuesTest {
    Map<String,String> map;

    @Before
    public void setUp() {
        map = new MyHashMap<>();
        map.put("j", "java");
        map.put("c", "c++");
        map.put("p", "python");
        map.put("n", "node");
    }

    @Test
    public void testSize() {
        assertEquals(4, map.size());
        int n = 1_000;
        for (int i = 0; i < n; i++) {
            map.put("k" + i, "v" + i );
        }
        assertEquals(n + 4, map.size());
    }
    @Test
    public void testContainsValue() {
        assertTrue(map.containsValue("java"));
        assertTrue(map.containsValue("c++"));
        assertTrue(map.containsValue("python"));
        assertFalse(map.containsValue("php"));
        String before = map.put("p", "php");
        assertEquals("python", before);
        assertTrue(map.containsValue("php"));
    }

    @Test
    public void testNotContainsValue() {
        assertFalse(map.containsValue("pascal"));
        assertFalse(map.containsValue("php"));
        assertFalse(map.containsValue("javascript"));
        assertFalse(map.containsKey("meh"));
        map.put("meh", "javascript");
        assertTrue(map.containsKey("meh"));
        assertTrue(map.containsValue("javascript"));
    }

    @Test
    public void testContainsKey() {
        assertTrue(map.containsKey("n"));
        assertTrue(map.containsKey("j"));
        assertFalse(map.containsKey("a"));
        map.put("a", "assembly");
        assertEquals(5, map.size());
        assertTrue(map.containsKey("a"));
    }

    @Test
    public void testClearAndIsEmpty() {
        map.clear();
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        String remove = map.remove("p");
        assertEquals(3, map.size());
        assertEquals("python", remove);
        assertNull(map.remove("cobol"));
        assertEquals(3, map.size());
    }
}
