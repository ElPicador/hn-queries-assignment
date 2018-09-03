package com.algolia.assignment.model.util;

import java.util.Objects;

/**
 * A simple value class wrapping an int.
 */
public final class MutableInt extends Number {

    private int value;

    public MutableInt(int aValue){
        this.value= aValue;
    }

    public MutableInt() {
        this(0);
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    public void increment() {
        value++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableInt that = (MutableInt) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "MutableInt{" +
                "value=" + value +
                '}';
    }
}
