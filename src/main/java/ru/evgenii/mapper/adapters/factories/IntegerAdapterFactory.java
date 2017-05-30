package ru.evgenii.mapper.adapters.factories;

import ru.evgenii.mapper.adapters.types.IntegerTypeAdapter;
import ru.evgenii.mapper.adapters.types.TypeAdapter;

public class IntegerAdapterFactory implements AdapterFactory {
    @Override
    public TypeAdapter getTypeAdapter() {
        return new IntegerTypeAdapter();
    }

    @Override
    public boolean isEqualClass(Class tClass) {
        //return tClass instanceof java.util.Currency.class;
        return Integer.class.equals(tClass)  || int.class.equals(tClass);
    }
}
