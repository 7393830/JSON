package ru.evgenii.mapper.reflection.adapters.types;

import java.math.BigDecimal;
import ru.evgenii.parser.JSONElement;

public class BigDecimalTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return BigDecimal.class.equals(tClass);
    }
    @Override
    public BigDecimal convert(JSONElement element) {
        return element.getAsBigDecimal();
    }
}
