package ru.evgenii.mapper.adapters.types;
import java.math.BigDecimal;
import ru.evgenii.JSONElement;

public class BigDecimalTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return BigDecimal.class.equals(tClass);
    }
    @Override
    public BigDecimal convert(JSONElement str) {
        return str.getAsBigDecimal();
    }
}
