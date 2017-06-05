package ru.evgenii.mapper.reflection.types;

import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import ru.evgenii.parser.JSONElement;

public class MapTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return Map.class.equals(tClass);
    }
    @Override
    public Map<String, Object> convert(JSONElement jsonElement) {
        Map<String, Object> tmpMap;
        if (jsonElement.isJsonObject()) {
            tmpMap = new LinkedTreeMap<>();
            for (Map.Entry<String, JSONElement> next : jsonElement.getAsJsonObject()) {
                JSONElement je = next.getValue();
                if(je.isJsonPrimitive())
                {
                    tmpMap.put(next.getKey(), je.getAsString());
                }

            }
        } else {
            throw new IllegalArgumentException("error. MapTypeAdapter.convert");
        }
        return tmpMap;
    }
}