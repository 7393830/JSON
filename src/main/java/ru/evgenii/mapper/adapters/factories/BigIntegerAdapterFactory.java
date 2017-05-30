package ru.evgenii.mapper.adapters.factories;

import java.math.BigInteger;
import ru.evgenii.mapper.adapters.types.BigIntegerTypeAdapter;
import ru.evgenii.mapper.adapters.types.TypeAdapter;

public class BigIntegerAdapterFactory implements AdapterFactory {
    @Override
    public TypeAdapter getTypeAdapter() {
        return new BigIntegerTypeAdapter();
    }

    @Override
    public boolean isEqualClass(Class tClass) {
        //return tClass instanceof java.util.Currency.class;
        return BigInteger.class.equals(tClass);
    }
}
