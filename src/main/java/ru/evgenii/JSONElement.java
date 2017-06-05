package ru.evgenii;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class JSONElement {

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

    boolean isJsonNull() {
        return this instanceof JSONNull;
    }

    public JSONObject getAsJsonObject() {
        if (isJsonObject()) {
            return (JSONObject) this;
        }
        throw new IllegalStateException("Not a JSON Object: " + this);
    }

    JSONArray getAsJsonArray() {
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

    public BigDecimal getAsBigDecimal() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public BigInteger getAsBigInteger() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    public boolean getAsBoolean() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    JSONNull getAsJsonNull() {
        if (isJsonNull()) {
            return (JSONNull) this;
        }
        throw new IllegalStateException("This is not a JSON Null.");
    }
}
