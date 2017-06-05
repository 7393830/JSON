package ru.evgenii.mapper.reflection.adapters.types;

import ru.evgenii.parser.JSONElement;

public class IntegerTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return Integer.class.equals(tClass)  || int.class.equals(tClass);
    }
    @Override
    public Integer convert(JSONElement element) {
      return element.getAsInt();
    }
}
