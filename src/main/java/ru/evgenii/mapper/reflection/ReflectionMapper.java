
package ru.evgenii.mapper.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.evgenii.parser.JSONElement;
import ru.evgenii.parser.JSONObject;
import ru.evgenii.mapper.reflection.adapters.types.BigDecimalTypeAdapter;
import ru.evgenii.mapper.reflection.adapters.types.BigIntegerTypeAdapter;
import ru.evgenii.mapper.reflection.adapters.types.CurrencyTypeAdapter;
import ru.evgenii.mapper.reflection.adapters.types.IntegerTypeAdapter;
import ru.evgenii.mapper.reflection.adapters.types.LongTypeAdapter;
import ru.evgenii.mapper.reflection.adapters.types.MapTypeAdapter;
import ru.evgenii.mapper.reflection.adapters.types.StringTypeAdapter;
import ru.evgenii.mapper.reflection.adapters.types.TypeAdapter;
import ru.evgenii.mapper.reflection.annotations.JSONCreator;
import ru.evgenii.mapper.reflection.annotations.JSONField;

public class ReflectionMapper {

    private List<TypeAdapter> typeAdapterList = new ArrayList<>();

    public ReflectionMapper() {
        typeAdapterList.add(new CurrencyTypeAdapter());
        typeAdapterList.add(new StringTypeAdapter());
        typeAdapterList.add(new IntegerTypeAdapter());
        typeAdapterList.add(new BigDecimalTypeAdapter());
        typeAdapterList.add(new BigIntegerTypeAdapter());
        typeAdapterList.add(new LongTypeAdapter());
        typeAdapterList.add(new MapTypeAdapter());
    }

    public String fieldNameOf(String fieldName) {
        return sbFieldNameOf(fieldName).toString();
    }

    public String getterNameOf(String fieldName) {
        StringBuffer sb = sbFieldNameOf(fieldName);
        Character ch = sb.charAt(0);
        if (Character.isLowerCase(ch)) {
            sb.replace(0, 1, "get" + Character.toUpperCase(ch));
        }
        return sb.toString();
    }

    private StringBuffer sbFieldNameOf(String fieldName) {
        Pattern p = Pattern.compile("(_[a-z])");
        Matcher m = p.matcher(fieldName);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        deleteUnderscore(sb);
        return sb;
    }

    private void deleteUnderscore(StringBuffer stringBuffer) {
        String from = "_";
        int index = stringBuffer.indexOf(from);
        while (index != -1) {
            stringBuffer.replace(index, index + from.length(), "");
            index = stringBuffer.indexOf(from, index);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T createObject(JSONElement je, Class<T> tClass) {
        try {
            T objT;
            Constructor checkAnnotationConstructor = checkAnnotationConstructor(tClass, JSONCreator.class);
            if (checkAnnotationConstructor != null) {
                Object[] arrayObjectForConstruct = getArrayObjectForConstruct(checkAnnotationConstructor, je);
                objT = (T) checkAnnotationConstructor.newInstance(arrayObjectForConstruct);
            } else {
                objT = tClass.newInstance();
                mappingJSON(je, objT);
            }
            return objT;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new IllegalArgumentException("error. ReflectionMapper.createObject. Message: " + e.getMessage());
        }
    }

    private Constructor checkAnnotationConstructor(Class argClass, Class<? extends Annotation> argAnnotationClass) {
        Constructor[] constructors = argClass.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.isAnnotationPresent(argAnnotationClass)) {
                return constructor;
            }
        }
        return null;
    }

    private Object[] getArrayObjectForConstruct(Constructor checkAnnotationConstructor, JSONElement je) {
        if (!je.isJsonObject()) {
            throw new IllegalArgumentException("error. ReflectionMapper.getArrayObjectForConstruct. Message: Input object must be JSONObject");
        }
        Parameter[] parameters = checkAnnotationConstructor.getParameters();
        Object[] objects = new Object[parameters.length];
        int i = 0;
        JSONObject jsonObject = je.getAsJsonObject();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(JSONField.class)) {
                String name = parameter.getAnnotation(JSONField.class).name();
                boolean required = parameter.getAnnotation(JSONField.class).required();
                Class<?> parameterType = parameter.getType();

                JSONElement jsonObjectByName = jsonObject.get(name);
                if (jsonObjectByName == null) {
                    if (!required) {
                        objects[i] = null;
                    } else {
                        throw new IllegalArgumentException("error. Message: Parameter " + parameter.getName() + " is required");
                    }
                } else {
                    TypeAdapter typeAdapter = factoryTypeAdapter(parameterType);
                    if (typeAdapter != null) {
                        objects[i] = typeAdapter.convert(jsonObjectByName);
                    } else {
                        objects[i] = createObject(jsonObjectByName, parameterType);
                    }

                }
            } else {
                throw new IllegalArgumentException("error. Message: Requested annotation is not found");
            }
            i++;
        }
        if (jsonObject.count() > parameters.length) {
            throw new IllegalArgumentException("error. there is no such parameter");
        }
        return objects;
    }

    private <T> void mappingJSON(JSONElement je, T tObj) throws NoSuchFieldException, IllegalAccessException {
        if (je.isJsonObject()) {
            JSONObject jsonObject = je.getAsJsonObject();
            for (Map.Entry<String, JSONElement> next : jsonObject) {
                String key = next.getKey();
                JSONElement value = next.getValue();

                Field field = tObj.getClass().getDeclaredField(key);
                if (field != null) {
                    Class<?> fieldType = field.getType();
                    TypeAdapter typeAdapter = factoryTypeAdapter(fieldType);
                    field.setAccessible(true);
                    if (typeAdapter != null) {
                        field.set(tObj, typeAdapter.convert(value));
                    } else {
                        field.set(tObj, createObject(value, fieldType));
                    }
                } else {
                    throw new IllegalArgumentException("error. Message: There is no such parameter");
                }
            }
        }
    }

    private TypeAdapter factoryTypeAdapter(Class<?> tClass) {
        for (TypeAdapter typeAdapter : typeAdapterList) {
            if (typeAdapter.isEqualClass(tClass)) {
                return typeAdapter;
            }
        }
        return null;
    }
}