package ru.evgenii.mapper.adapters.factories;


import ru.evgenii.mapper.Currency;
import ru.evgenii.mapper.adapters.types.CurrencyTypeAdapter;
import ru.evgenii.mapper.adapters.types.TypeAdapter;

public class CurrencyAdapterFactory implements AdapterFactory{

    @Override
    public TypeAdapter getTypeAdapter() {
        return new CurrencyTypeAdapter();
    }

    @Override
    public boolean isEqualClass(Class tClass) {
        //return tClass instanceof java.util.Currency.class;
       return Currency.class.equals(tClass);
    }
}
