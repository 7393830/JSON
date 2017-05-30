package ru.evgenii.mapper.adapters.types;


import ru.evgenii.JSONElement;
import ru.evgenii.mapper.Currency;

public class CurrencyTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return Currency.class.equals(tClass);
    }
    @Override
    public Currency convert(JSONElement str) {
        for (Currency dir : Currency.values()) {
            if(str.getAsString().equals(dir.toString())) {
                return dir;
            }
        }
        throw new IllegalArgumentException("error. Illegal input currency.");
    }
}
