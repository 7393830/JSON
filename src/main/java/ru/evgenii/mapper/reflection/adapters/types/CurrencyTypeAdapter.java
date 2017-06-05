package ru.evgenii.mapper.reflection.adapters.types;

import ru.evgenii.parser.JSONElement;
import ru.evgenii.mapper.reflection.Currency;

public class CurrencyTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return Currency.class.equals(tClass);
    }
    @Override
    public Currency convert(JSONElement element) {
        for (Currency currency : Currency.values()) {
            if(element.getAsString().equals(currency.toString())) {
                return currency;
            }
        }
        throw new IllegalArgumentException("error. CurrencyTypeAdapter.convert. Message: Illegal input currency");
    }
}
