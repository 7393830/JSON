package ru.evgenii.mapper.reflection.adapters.types;

import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;
import ru.evgenii.parser.JSONElement;

public class MapTypeAdapter implements TypeAdapter {
    @Override
    public boolean isEqualClass(Class tClass) {
        return Map.class.equals(tClass);
    }
    @Override
    public Map<String, Object> convert(JSONElement element) {
        Map<String, Object> tmpMap;
        if (element.isJsonObject()) {
            tmpMap = new LinkedTreeMap<>();
            for (Map.Entry<String, JSONElement> next : element.getAsJsonObject()) {
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