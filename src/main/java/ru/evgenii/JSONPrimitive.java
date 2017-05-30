package ru.evgenii;

import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;


public class JSONPrimitive extends JSONElement {

    protected Object value;

    JSONPrimitive() {
    }

    JSONPrimitive(String string) {
        setValue(string);
    }

    JSONPrimitive(Boolean bool) {
        setValue(bool);
    }

    JSONPrimitive(Number number) {
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
        return value instanceof String ? new LazilyParsedNumber((String) value) : (Number) value;//return (Number) value;
    }

    @Override
    public double getAsDouble() {

        if (isNumber()) {
            return getAsNumber().doubleValue();
        } else {
            throw new NumberFormatException("error. Not Double");
        }
    }

    @Override
    public int getAsInt() {

        if (isNumber()) {
            return getAsNumber().intValue();
        } else {
            throw new NumberFormatException("error. Not Int");
        }
    }

    @Override
    public long getAsLong() {
        if (isNumber()) {
            return getAsNumber().longValue();
        } else {
            throw new NumberFormatException("error. Not Long");
        }
    }


    @Override
    public float getAsFloat() {
        if (isNumber()) {
            return getAsNumber().floatValue();
        } else {
            throw new NumberFormatException("error. Not Float");
        }
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        return value instanceof BigDecimal ? (BigDecimal) value : new BigDecimal(value.toString());
    }

    @Override
    public BigInteger getAsBigInteger() {
        return value instanceof BigInteger ?
                (BigInteger) value : new BigInteger(value.toString());
    }

    @Override
    public String getAsString() {
        if (isNumber()) {
            return getAsNumber().toString();
        } else if (isBoolean()) {
            return value.toString();
        } else {
            return (String) value;
        }
    }

    @Override
    public boolean getAsBoolean() {
        if (isBoolean()) {
            return (Boolean)value;
        } else {
            throw new IllegalStateException("error. Not Boolean");
        }
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JSONPrimitive other = (JSONPrimitive) obj;
        if (value == null) {
            return other.value == null;
        }
        if (isIntegral(this) && isIntegral(other)) {
            return getAsNumber().longValue() == other.getAsNumber().longValue();
        }
        if (value instanceof Number && other.value instanceof Number) {
            double a = getAsNumber().doubleValue();
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
