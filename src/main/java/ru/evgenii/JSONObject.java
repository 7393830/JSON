package ru.evgenii;


import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;

/**
 * Created by e.kostyukovskiy on 03.04.2017.
 */
public class JSONObject extends JSONElement {

    private LinkedTreeMap<String, JSONElement> members;

    public JSONObject() {
        members = new LinkedTreeMap<String, JSONElement>();
    }

    public JSONElement get(String argStr) {
        return members.get(argStr);
    }

    public JSONPrimitive getAsJsonPrimitive(String argStr)
    {
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
