package ru.evgenii.mapper.reflection.adapters.types;

import ru.evgenii.parser.JSONElement;

public class StringTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return String.class.equals(tClass);
    }

    @Override
    public String convert(JSONElement element) {
        return element.getAsString();
    }
}
