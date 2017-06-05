package ru.evgenii.parser;


import com.google.gson.internal.LinkedTreeMap;
import java.util.Iterator;
import java.util.Map;


public class JSONObject extends JSONElement implements Iterable<Map.Entry<String, JSONElement>> {

    private LinkedTreeMap<String, JSONElement> members;

    JSONObject() {
        members = new LinkedTreeMap<>();
    }

    public JSONElement get(String argStr) {
        return members.get(argStr);
    }

    public int count() {
        return members.size();
    }

    public Iterator<Map.Entry<String, JSONElement>> iterator() {
        return members.entrySet().iterator();
    }

    public void add(String property, JSONElement value) {
        if (value == null) {
            value = JSONNull.INSTANCE;
        }
        members.put(property, value);
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof JSONObject
                && ((JSONObject) o).members.equals(members));
    }

    public Iterable<Map.Entry<String, JSONElement>> entrySet() {
        return members.entrySet();
    }
}
