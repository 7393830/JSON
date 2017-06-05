package ru.evgenii.mapper.reflection.types;

import ru.evgenii.parser.JSONElement;

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
