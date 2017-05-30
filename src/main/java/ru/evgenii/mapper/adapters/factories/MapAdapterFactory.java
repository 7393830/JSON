package ru.evgenii.mapper.adapters.factories;

import java.util.Map;
import ru.evgenii.mapper.adapters.types.MapTypeAdapter;
import ru.evgenii.mapper.adapters.types.TypeAdapter;

public class MapAdapterFactory implements AdapterFactory {
    @Override
    public TypeAdapter getTypeAdapter() {
        return new MapTypeAdapter();
    }

    @Override
    public boolean isEqualClass(Class tClass) {
        //return tClass instanceof java.util.Currency.class;
        return Map.class.equals(tClass);
    }
}
