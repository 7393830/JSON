package ru.evgenii.mapper.adapters.types;

import ru.evgenii.JSONElement;

public interface TypeAdapter {
    boolean isEqualClass(Class tClass);
    Object convert(JSONElement str);
}
