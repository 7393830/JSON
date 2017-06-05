package ru.evgenii.mapper.reflection.types;

import java.math.BigInteger;
import ru.evgenii.parser.JSONElement;

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
