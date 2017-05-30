package ru.evgenii.mapper.adapters.factories;

import ru.evgenii.mapper.adapters.types.TypeAdapter;

public interface AdapterFactory {
    TypeAdapter getTypeAdapter();
    boolean isEqualClass(Class tClass);
}
