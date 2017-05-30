package ru.evgenii.mapper.adapters.factories;

import java.math.BigDecimal;
import ru.evgenii.mapper.adapters.types.BigDecimalTypeAdapter;
import ru.evgenii.mapper.adapters.types.TypeAdapter;

public class BigDecimalAdapterFactory implements AdapterFactory {
    @Override
    public TypeAdapter getTypeAdapter() {
        return new BigDecimalTypeAdapter();
    }

    @Override
    public boolean isEqualClass(Class tClass) {
        return BigDecimal.class.equals(tClass);
    }
}
