package ru.evgenii;

import ru.evgenii.parser.JSONElement;

public interface Mapper<T> {
    T map(JSONElement parse) throws NoSuchFieldException, IllegalAccessException;
}
