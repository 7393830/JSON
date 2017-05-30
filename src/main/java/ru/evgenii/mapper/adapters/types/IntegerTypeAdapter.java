package ru.evgenii.mapper.adapters.types;

import ru.evgenii.JSONElement;

public class IntegerTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return Integer.class.equals(tClass)  || int.class.equals(tClass);
    }
    @Override
    public Integer convert(JSONElement str) {
      return str.getAsInt();
    }
}
