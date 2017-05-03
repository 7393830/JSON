package ru.evgenii;

import com.google.gson.JsonElement;

import java.io.StringReader;
import java.util.Deque;
import java.util.List;

/**
 * Created by e.kostyukovskiy on 03.04.2017.
 */
public class JSONElement {

    public JSONElement() {
    }

    public boolean isJsonObject() {
        return this instanceof JSONObject;
    }

    public boolean isJsonArray() {
        return this instanceof JSONArray;
    }

    public boolean isJsonPrimitive() {
        return this instanceof JSONPrimitive;
    }

    public boolean isJsonNull() {
        return this instanceof JSONNull;
    }

    public JSONObject getAsJsonObject() {
        if (isJsonObject()) {
            return (JSONObject) this;
        }
        throw new IllegalStateException("Not a JSON Object: " + this);
    }

    public JSONArray getAsJsonArray() {
        if (isJsonArray()) {
            return (JSONArray) this;
        }
        throw new IllegalStateException("This is not a JSON Array.");
    }

    public JSONPrimitive getAsJsonPrimitive() {
        if (isJsonPrimitive()) {
            return (JSONPrimitive) this;
        }
        throw new IllegalStateException("This is not a JSON Primitive.");
    }

    public String getAsString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public Number getAsNumber() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public long getAsLong() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public int getAsInt() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public double getAsDouble() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public float getAsFloat() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public boolean getAsBoolean() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    Boolean getAsBooleanWrapper() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public JSONNull getAsJsonNull() {
        if (isJsonNull()) {
            return (JSONNull) this;
        }
        throw new IllegalStateException("This is not a JSON Null.");
    }
}
