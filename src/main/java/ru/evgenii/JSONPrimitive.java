package ru.evgenii;

import java.math.BigInteger;

/**
 * Created by e.kostyukovskiy on 03.04.2017.
 */
public class JSONPrimitive extends JSONElement {

    protected Object value;

    public JSONPrimitive() {
    }

    public JSONPrimitive(String string) {
        setValue(string);
    }

    public JSONPrimitive(Boolean bool) {
        setValue(bool);
    }

    public JSONPrimitive(Number number) {
        setValue(number);
    }
    public boolean isNumber() {
        return value instanceof Number;
    }

    void setValue(Object primitive) {
        this.value = primitive;
    }

    @Override
    public Number getAsNumber() {
        return (Number) value;
    }

    @Override
    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    @Override
    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override
    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    @Override
    public String getAsString() {
        return (String) value;
    }

    @Override
    public boolean getAsBoolean() {
        if (isBoolean()) {
            return getAsBooleanWrapper().booleanValue();
        } else {
            // Check to see if the value as a String is "true" in any case.
            throw new IllegalStateException("error. Not Boolean");
            //return Boolean.parseBoolean(getAsString());
        }
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    @Override
    Boolean getAsBooleanWrapper() {
        return (Boolean) value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JSONPrimitive other = (JSONPrimitive)obj;
        if (value == null) {
            return other.value == null;
        }
        if (isIntegral(this) && isIntegral(other)) {
            return getAsNumber().longValue() == other.getAsNumber().longValue();
        }
        if (value instanceof Number && other.value instanceof Number) {
            double a = getAsNumber().doubleValue();
            // Java standard types other than double return true for two NaN. So, need
            // special handling for double.
            double b = other.getAsNumber().doubleValue();
            return a == b || (Double.isNaN(a) && Double.isNaN(b));
        }
        return value.equals(other.value);
    }
    private static boolean isIntegral(JSONPrimitive primitive) {
        if (primitive.value instanceof Number) {
            Number number = (Number) primitive.value;
            return number instanceof BigInteger || number instanceof Long || number instanceof Integer
                    || number instanceof Short || number instanceof Byte;
        }
        return false;
    }
}
