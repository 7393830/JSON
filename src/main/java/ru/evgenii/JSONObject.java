package ru.evgenii;


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

    public JSONPrimitive getAsJsonPrimitive(String argStr) {
        return (JSONPrimitive) members.get(argStr);
    }

    public void add(String property, JSONElement value) {
        if (value == null) {
            value = JSONNull.INSTANCE;
        }
        members.put(property, value);
    }

    public JSONObject getAsJsonObject(String memberName) {
        return (JSONObject) members.get(memberName);
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof JSONObject
                && ((JSONObject) o).members.equals(members));
    }
}
