
package ru.evgenii.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.evgenii.JSONElement;
import ru.evgenii.JSONObject;
import ru.evgenii.mapper.adapters.factories.AdapterFactory;
import ru.evgenii.mapper.adapters.factories.BigDecimalAdapterFactory;
import ru.evgenii.mapper.adapters.factories.BigIntegerAdapterFactory;
import ru.evgenii.mapper.adapters.factories.CurrencyAdapterFactory;
import ru.evgenii.mapper.adapters.factories.IntegerAdapterFactory;
import ru.evgenii.mapper.adapters.factories.LongAdapterFactory;
import ru.evgenii.mapper.adapters.factories.MapAdapterFactory;
import ru.evgenii.mapper.adapters.factories.StringAdapterFactory;
import ru.evgenii.mapper.adapters.types.TypeAdapter;
import ru.evgenii.mapper.annotations.JSONCreator;
import ru.evgenii.mapper.annotations.JSONField;

public class ReflectionMapper {
    public <T> T createObject(JSONElement je, Class<T> simplePOJOClass) {
        try {
            T objT;
            Constructor checkAnnotationConstructor = checkAnnotationConstructor(simplePOJOClass, JSONCreator.class);
            if (checkAnnotationConstructor != null) {
                List<Object> objects = vernutSpisokObektov(checkAnnotationConstructor, je);

                Object[] fff = new Object[objects.size()];
                for(int i= 0; i< objects.size();i++) {
                    fff[i] = objects.get(i);
                }
                objT = (T) checkAnnotationConstructor.newInstance(fff);//( new Object[]{valuesFromJSON});

            } else {
                objT = simplePOJOClass.newInstance();
                proxodPoJSON(je, simplePOJOClass, objT);
            }
            return objT;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new IllegalArgumentException("error.");
        }
    }

    private List<Object> vernutSpisokObektov(Constructor checkAnnotationConstructor, JSONElement je) {
        if (!je.isJsonObject()) {
            throw new IllegalArgumentException("error.");
        }
        List<Object> objects = new ArrayList<>();
        Parameter[] parameters = checkAnnotationConstructor.getParameters();
        //int counterParameters = 0;
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(JSONField.class)) {
                String name = parameter.getAnnotation(JSONField.class).name();
                boolean required = parameter.getAnnotation(JSONField.class).required();
                Class clazz = parameter.getType();

                JSONObject jsonObject = je.getAsJsonObject();

                JSONElement jsel = jsonObject.get(name);
                if (jsel == null) {
                    if (!required) {
                        objects.add(null);
                    } else {
                        throw new IllegalArgumentException("error.");
                    }
                } else {
                    objects.add(factory(clazz).convert(jsel));
                }
            }
            else
            {
                throw new IllegalArgumentException("error.");
            }
        }
        if(objects.size() != parameters.length)
        {
            throw new IllegalArgumentException("error.");
        }
        return objects;
    }

    private void proxodPoJSON(JSONElement je, Class simplePOJOClass, Object obj) throws NoSuchFieldException, IllegalAccessException {
        if (je.isJsonObject()) {
            JSONObject jsonObject = je.getAsJsonObject();
            for (Map.Entry<String, JSONElement> next : jsonObject) {
                String key = next.getKey();
                JSONElement value = next.getValue();

                Field field = simplePOJOClass.getDeclaredField(key);
                if (field != null) {
                    Class type = field.getType();
                    TypeAdapter typeAdapter = factory(type);
                    field.setAccessible(true);
                    if (typeAdapter != null) {
                        field.set(obj, typeAdapter.convert(value));
                    } else {
                        field.set(obj, createObject(value, type));
                    }
                } else {
                    throw new IllegalArgumentException("error.");
                }
            }
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


    Map<String, TypeAdapter> myMap = null;
    private List<AdapterFactory> adapterFactoryList = new ArrayList<>();

    public ReflectionMapper() {
        this.adapterFactoryList = initAdapterFactory();
    }

    private List<AdapterFactory> initAdapterFactory() {
        List<AdapterFactory> adapterFactories = new ArrayList<>();
        adapterFactories.add(new CurrencyAdapterFactory());
        adapterFactories.add(new StringAdapterFactory());
        adapterFactories.add(new IntegerAdapterFactory());
        adapterFactories.add(new BigDecimalAdapterFactory());
        adapterFactories.add(new BigIntegerAdapterFactory());
        adapterFactories.add(new LongAdapterFactory());
        adapterFactories.add(new MapAdapterFactory());
        return adapterFactories;
    }

    public static String fieldNameOf(String nothingToDo) {
        return sbFieldNameOf(nothingToDo).toString();
    }

    public static String getterNameOf(String idempotent) {
        StringBuffer sb = sbFieldNameOf(idempotent);
        Character ch = sb.charAt(0);
        if (Character.isLowerCase(ch)) {
            sb.replace(0, 1, "get" + Character.toUpperCase(ch));
        }
        return sb.toString();
    }

    private static StringBuffer sbFieldNameOf(String nothingToDo) {
        Pattern p = Pattern.compile("(_[a-z])");
        Matcher m = p.matcher(nothingToDo);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);
        deleteUnderscore(sb);
        return sb;
    }

    private static void deleteUnderscore(StringBuffer builder) {
        String from = "_";
        int index = builder.indexOf(from);
        while (index != -1) {
            builder.replace(index, index + from.length(), "");
            index = builder.indexOf(from, index);
        }
    }

    public TypeAdapter factory(Class tClass) {
        for (AdapterFactory factory : adapterFactoryList) {
            if (factory.isEqualClass(tClass)) {
                return factory.getTypeAdapter();
            }
        }
        //throw new NotImplementedException();
        return null;
    }

    List<AnotetionFieldMatching> stringAnotetionFieldMatchingMap;

    private Object fillMyMap(Class simplePOJOClass) throws IllegalAccessException, InstantiationException {
        myMap = new TreeMap<>();
        stringAnotetionFieldMatchingMap = new ArrayList<>();
        Object obj;
        Constructor[] constructors = simplePOJOClass.getConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.isAnnotationPresent(JSONCreator.class)) {
                Parameter[] parameters = constructor.getParameters();
                for (Parameter parameter : parameters) {
                    if (parameter.isAnnotationPresent(JSONField.class)) {
                        stringAnotetionFieldMatchingMap.add(
                                new AnotetionFieldMatching(
                                        parameter.getAnnotation(JSONField.class).name(),
                                        parameter.getAnnotation(JSONField.class).required()
                                ));
                    }
                }
            }
        }
        obj = simplePOJOClass.newInstance();


        Field[] publicFields = simplePOJOClass.getDeclaredFields();
        for (Field field : publicFields) {
            //field.setAccessible(true);
            Class fieldType = field.getType();
            myMap.put(field.getName(), factory(fieldType));
        }
        return obj;
    }

    public <T> T createObject2(JSONElement je, Class<T> simplePOJOClass) {

        try {
            Object obj = fillMyMap(simplePOJOClass);
            if (je.isJsonObject()) {
                JSONObject jsonObject = je.getAsJsonObject();
                for (Map.Entry<String, JSONElement> next : jsonObject) {
                    String key = next.getKey();
                    JSONElement value = next.getValue();
                    if (value.isJsonPrimitive()) {
                        if (myMap.containsKey(key)) {
                            Field field = simplePOJOClass.getDeclaredField(key);
                            field.setAccessible(true);
                            field.set(obj, myMap.get(key).convert(value));
                        } else {
                            throw new IllegalArgumentException("error. JSONElement overrides the type.");
                        }
                    } else {
                        if (value.isJsonObject()) {
                            if (myMap.containsKey(key)) {
                                Field field = simplePOJOClass.getDeclaredField(key);
                                field.setAccessible(true);
                                field.set(obj, myMap.get(key).convert(value));
                            } else {
                                throw new IllegalArgumentException("error. JSONElement overrides the type.");
                            }
                        } else {
                            if (value.isJsonArray()) {

                            } else {

                            }
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Error! ReflectionMapper.createObject");
            }
            return (T) obj;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException("Error! ReflectionMapper.createObject");
        }
    }

    public <T> T createObject1(JSONElement je, Class<T> simplePOJOClass) {


        try {
            T t = simplePOJOClass.newInstance();
            Object obj = simplePOJOClass.newInstance();

            if (je.isJsonObject()) {
                JSONObject jsonObject = je.getAsJsonObject();
                for (Map.Entry<String, JSONElement> next : jsonObject) {
                    String key = next.getKey();
                    JSONElement value = next.getValue();
                    if (value.isJsonPrimitive()) {
                        Field field = simplePOJOClass.getDeclaredField(key);
                        field.setAccessible(true);
                        Class fieldType = field.getType();
                        String tmpNameField = fieldType.getName().toLowerCase();
                        if (tmpNameField.contains("int"))
                            field.set(obj, value.getAsInt());
                        else if (tmpNameField.contains("boolean"))
                            field.set(obj, value.getAsBoolean());
                        else if (tmpNameField.contains("double"))
                            field.set(obj, value.getAsDouble());
                        else if (tmpNameField.contains("float"))
                            field.set(obj, value.getAsFloat());
                        else if (tmpNameField.contains("long"))
                            field.set(obj, value.getAsLong());
                        else
                            //if (tmpNameField.contains("string"))
                            field.set(obj, value.getAsString());
                    } else {
                        if (value.isJsonObject()) {

                        } else {
                            if (value.isJsonArray()) {

                            } else {

                            }
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Error! ReflectionMapper.createObject");
            }
            return (T) obj;
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException("Error! ReflectionMapper.createObject");
        }
    }
}

