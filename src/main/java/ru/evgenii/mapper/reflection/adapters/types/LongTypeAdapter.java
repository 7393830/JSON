package ru.evgenii.mapper.reflection.types;

import ru.evgenii.parser.JSONElement;

public class LongTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return Long.class.equals(tClass) || long.class.equals(tClass);
    }
    @Override
    public Long convert(JSONElement str) {
        return str.getAsLong();
    }
}
