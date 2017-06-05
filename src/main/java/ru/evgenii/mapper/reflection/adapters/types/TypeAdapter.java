package ru.evgenii.mapper.reflection.types;

import ru.evgenii.parser.JSONElement;

public interface TypeAdapter {
    boolean isEqualClass(Class tClass);
    Object convert(JSONElement str);
}
