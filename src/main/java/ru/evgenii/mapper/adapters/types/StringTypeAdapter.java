package ru.evgenii.mapper.adapters.types;

import ru.evgenii.JSONElement;

public class StringTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return String.class.equals(tClass);
    }

    @Override
    public String convert(JSONElement str) {
        return str.getAsString();
    }
}
