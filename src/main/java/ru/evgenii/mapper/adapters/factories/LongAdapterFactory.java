package ru.evgenii.mapper.adapters.factories;

import ru.evgenii.mapper.adapters.types.LongTypeAdapter;
import ru.evgenii.mapper.adapters.types.TypeAdapter;

public class LongAdapterFactory implements AdapterFactory {
    @Override
    public TypeAdapter getTypeAdapter() {
        return new LongTypeAdapter();
    }

    @Override
    public boolean isEqualClass(Class tClass) {
        //return tClass instanceof java.util.Currency.class;
        return Long.class.equals(tClass) || long.class.equals(tClass);
    }
}
