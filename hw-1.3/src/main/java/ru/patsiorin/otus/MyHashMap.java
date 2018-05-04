package ru.patsiorin.otus;

import java.util.*;

/**
 * <p>
 * This Map interface implementation uses
 * <a href="https://en.wikipedia.org/wiki/Open_addressing">Open Addressing to resolve</a>
 * hash collisions.
 * </p>
 *
 * <p>
 * To make things more simple objects are mapped to hashmap's internal buckets with
 * remainder after division key's object hashcode by current bucket array length.
 * </p>
 *
 * <p>
 * Null keys are not allowed. Null values are okay.
 * </p>
 *
 * @param <K> the type of keys
 * @param <V> the type of mapped values
 */
public class MyHashMap<K, V> implements Map<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.5f;
    private static final int MAXIMUM_CAPACITY = Integer.MAX_VALUE / 2;

    private int size;
    private int markedRemoved;

    private Node<K, V>[] buckets;
    private int capacity = DEFAULT_INITIAL_CAPACITY;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public MyHashMap(int initialCapacity) {
        capacity = initialCapacity;
        buckets = new Node[capacity];
    }

    public MyHashMap(Map<K,V> map) {
        for (Map.Entry<K,V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        K key;
        V value;

        public boolean isRemoved() {
            return removed;
        }

        public void markRemoved() {
            this.removed = true;
        }

        boolean removed;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            return this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) &&
                    Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {

            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(Object key) {
        Objects.requireNonNull(key, "Null keys not allowed");
        return getNode(key) != null;
    }

    public boolean containsValue(Object value) {
        for (Node<K, V> node : buckets) {
            if (node != null && !node.isRemoved()) {
                if (value == node.getValue() ||
                        (value != null && value.equals(node.getValue()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public V get(Object key) {
        Node<K, V> node = getNode(key);
        if (node == null) return null;
        return node.getValue();
    }

    @SuppressWarnings("unchecked")
    private Node<K, V> getNode(Object key) {
        int index = getIndex((K) key);
        int fullCircle = buckets.length;
        Node<K, V> node = null;
        // loop until every slot is checked
        while (fullCircle-- > 0) {
            node = buckets[index];
            if (node == null) return null;
            if (!node.isRemoved() && node.getKey().equals(key)) return node;
            if (++index >= buckets.length) {
                // end of the array, go from the beginning
                index %= buckets.length;
            }
        }
        return node;
    }

    public V put(K key, V value) {
        Objects.requireNonNull(key, "Null keys not allowed");

        Node<K, V> newNode = new Node<>(key, value);
        int index = getIndex(key);

        V ret = putVal(key, newNode, index);

        float loadFactor = LOAD_FACTOR;
        if (size + markedRemoved > capacity * loadFactor) {
            resize();
        }

        return ret;
    }

    private int getIndex(K key) {
        int hashCode;
        // hashCode may be negative
        hashCode = (hashCode = key.hashCode()) < 0 ? -hashCode : hashCode;
        return hashCode % buckets.length;
    }

    private V putVal(K key, Node<K, V> node, int index) {
        V ret = null;
        while (true) {
            Node<K, V> n = buckets[index];
            if (n == null) {
                buckets[index] = node;
                size++;
                break;
            } else if (!n.isRemoved() && n.getKey().equals(key)) {
                ret = n.getValue();
                buckets[index] = node;
                break;
            }

            // collision happened here, go to the next slot in the array
            // if the end of the array has been reached
            // try from the beginning
            if (++index >= buckets.length) {
                index %= buckets.length;
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        capacity <<= 1;

        if (capacity > MAXIMUM_CAPACITY) {
            capacity = MAXIMUM_CAPACITY;
        }

        Node<K, V>[] oldBuckets = buckets;
        buckets = new Node[capacity];
        size = 0;
        markedRemoved = 0;
        for (Node<K, V> node : oldBuckets) {
            if (node != null && !node.isRemoved()) {
                putVal(node.getKey(), node, getIndex(node.getKey()));
            }
        }
    }

    public V remove(Object key) {
        Objects.requireNonNull(key, "Null keys not allowed");

        Node<K, V> remove = getNode(key);
        if (remove == null) return null;
        if (!remove.isRemoved()) {
            remove.markRemoved();
            size--;
            markedRemoved++;
            return remove.getValue();
        }
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void clear() {
        size = 0;
        markedRemoved = 0;
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = null;
        }
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Node<K, V> node : buckets) {
            if (node != null && !node.isRemoved()) {
                keySet.add(node.getKey());
            }
        }
        return keySet;
    }

    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        for (Node<K, V> node : buckets) {
            if (node != null && !node.isRemoved()) {
                values.add(node.getValue());
            }
        }
        return values;
    }

    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entrySet = new HashSet<>();
        for (Node<K, V> node : buckets) {
            if (node != null && !node.isRemoved()) {
                entrySet.add(node);
            }
        }
        return entrySet;
    }

}
