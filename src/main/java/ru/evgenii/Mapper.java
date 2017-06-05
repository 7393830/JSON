package ru.evgenii;

public interface Mapper<T> {
    T map(JSONElement parse) throws NoSuchFieldException, IllegalAccessException;
}
