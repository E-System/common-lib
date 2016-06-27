package com.es.lib.common.model;

import java.util.Map;
import java.util.Objects;

/**
 * @author Zuzoev Dmitry - zuzoev.d@ext-system.com
 * @since 27.06.16
 */
public class MutableEntry<K, V> implements Map.Entry<K, V>, java.io.Serializable {

    private K key;
    private V value;

    /**
     * Creates an entry representing a mapping from the specified
     * key to the specified value.
     *
     * @param key   the key represented by this entry
     * @param value the value represented by this entry
     */
    public MutableEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Creates an entry representing the same mapping as the
     * specified entry.
     *
     * @param entry the entry to copy
     */
    public MutableEntry(Map.Entry<? extends K, ? extends V> entry) {
        this.key = entry.getKey();
        this.value = entry.getValue();
    }

    /**
     * Returns the key corresponding to this entry.
     *
     * @return the key corresponding to this entry
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the value corresponding to this entry.
     *
     * @return the value corresponding to this entry
     */
    public V getValue() {
        return value;
    }

    /**
     * Replaces the value corresponding to this entry with the specified
     * value.
     *
     * @param value new value to be stored in this entry
     * @return the old value corresponding to the entry
     */
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    /**
     * Replaces the key corresponding to this entry with the specified
     * value.
     *
     * @param key new key to be stored in this entry
     * @return the old key corresponding to the entry
     */
    public K setKey(K key) {
        K oldKey = this.key;
        this.key = key;
        return oldKey;
    }

    /**
     * Compares the specified object with this entry for equality.
     * Returns {@code true} if the given object is also a map entry and
     * the two entries represent the same mapping.  More formally, two
     * entries {@code e1} and {@code e2} represent the same mapping
     * if<pre>
     *   (e1.getKey()==null ?
     *    e2.getKey()==null :
     *    e1.getKey().equals(e2.getKey()))
     *   &amp;&amp;
     *   (e1.getValue()==null ?
     *    e2.getValue()==null :
     *    e1.getValue().equals(e2.getValue()))</pre>
     * This ensures that the {@code equals} method works properly across
     * different implementations of the {@code Map.Entry} interface.
     *
     * @param o object to be compared for equality with this map entry
     * @return {@code true} if the specified object is equal to this map
     * entry
     * @see #hashCode
     */
    public boolean equals(Object o) {
        if (!(o instanceof Map.Entry))
            return false;
        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
        return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
    }

    /**
     * Returns the hash code value for this map entry.  The hash code
     * of a map entry {@code e} is defined to be: <pre>
     *   (e.getKey()==null   ? 0 : e.getKey().hashCode()) ^
     *   (e.getValue()==null ? 0 : e.getValue().hashCode())</pre>
     * This ensures that {@code e1.equals(e2)} implies that
     * {@code e1.hashCode()==e2.hashCode()} for any two Entries
     * {@code e1} and {@code e2}, as required by the general
     * contract of {@link Object#hashCode}.
     *
     * @return the hash code value for this map entry
     * @see #equals
     */
    public int hashCode() {
        return (key == null ? 0 : key.hashCode()) ^
                (value == null ? 0 : value.hashCode());
    }

    /**
     * Returns a String representation of this map entry.  This
     * implementation returns the string representation of this
     * entry's key followed by the equals character ("<tt>=</tt>")
     * followed by the string representation of this entry's value.
     *
     * @return a String representation of this map entry
     */
    public String toString() {
        return key + "=" + value;
    }
}
