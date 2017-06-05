package ru.evgenii;

import java.math.BigDecimal;
import java.math.BigInteger;


public class JSONPrimitive extends JSONElement {

    protected Object value;

    JSONPrimitive(String string) {
        setValue(string);
    }

    JSONPrimitive(Boolean bool) {
        setValue(bool);
    }

    JSONPrimitive(Number number) {
        setValue(number);
    }

    void setValue(Object primitive) {
        this.value = primitive;
    }

    private boolean isNumberType() {
        return value instanceof Number;
    }

    private boolean isStringType() {
        return value instanceof String;
    }

    @Override
    public double getAsDouble() {
        if (isNumberType()) {
            return ((Number) value).doubleValue();
        } else {
            if (isStringType()) {
                return Double.valueOf((String) value);
            } else {
                throw new NumberFormatException("error. Not Double");
            }
        }
    }

    @Override
    public int getAsInt() {
        if (isNumberType()) {
            return ((Number) value).intValue();
        } else {
            if (isStringType()) {
                return Integer.decode((String) value);
            } else {
                throw new NumberFormatException("error. Not Int");
            }
        }
    }

    @Override
    public long getAsLong() {
        if (isNumberType()) {
            return ((Number) value).longValue();
        } else {
            if (isStringType()) {
                return Long.valueOf((String) value);
            } else {
                throw new NumberFormatException("error. Not Long");
            }
        }
    }

    @Override
    public float getAsFloat() {
        if (isNumberType()) {
            return ((Number) value).floatValue();
        } else {
            if (isStringType()) {
                return Float.valueOf((String) value);
            } else {
                throw new NumberFormatException("error. Not Float");
            }
        }
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        return value instanceof BigDecimal ? (BigDecimal) value : new BigDecimal(getAsString());
    }

    @Override
    public BigInteger getAsBigInteger() {
        return value instanceof BigInteger ?
                (BigInteger) value : new BigInteger(getAsString());
    }

    @Override
    public String getAsString() {
       return value.toString();
    }

    @Override
    public boolean getAsBoolean() {
        if (isBoolean()) {
            return (Boolean) value;
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
            return getAsLong() == other.getAsLong();
        }
        if (value instanceof Number && other.value instanceof Number) {
            double a = getAsDouble();
            double b = other.getAsDouble();
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
