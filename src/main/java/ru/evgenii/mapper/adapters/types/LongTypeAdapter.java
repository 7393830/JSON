package ru.evgenii.mapper.adapters.types;

import ru.evgenii.JSONElement;

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
