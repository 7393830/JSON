package ru.evgenii.mapper.adapters.types;

import java.math.BigInteger;
import ru.evgenii.JSONElement;

public class BigIntegerTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return BigInteger.class.equals(tClass);
    }
    @Override
    public BigInteger convert(JSONElement str) {
        return str.getAsBigInteger();
    }
}
