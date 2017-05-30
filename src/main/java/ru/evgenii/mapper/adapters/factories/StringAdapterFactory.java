package ru.evgenii.mapper.adapters.factories;

import ru.evgenii.mapper.adapters.types.StringTypeAdapter;
import ru.evgenii.mapper.adapters.types.TypeAdapter;

public class StringAdapterFactory implements AdapterFactory {
    @Override
    public TypeAdapter getTypeAdapter() {
        return new StringTypeAdapter();
    }

    @Override
    public boolean isEqualClass(Class tClass) {
        //return tClass instanceof java.util.Currency.class;
        return String.class.equals(tClass);
    }
}
