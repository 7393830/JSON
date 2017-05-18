package ru.evgenii;

import com.google.gson.internal.LazilyParsedNumber;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isDigit;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;

class JSONParser {
    private static final int END_READ_STREAM = -1;
    private static final int CHAR_MINUS = '-';
    private static final int DOUBLE_QUOTE = '"';
    private static final int FIRST_CHAR_TRUE = 't';
    private static final int FIRST_CHAR_FALSE = 'f';
    private static final int FIRST_CHAR_NULL = 'n';
    private static final int OPENING_SQUARE_BRACKET = '[';
    private static final int CLOSING_SQUARE_BRACKET = ']';
    private static final int OPENING_CURLY_BRACKET = '{';
    private static final int CLOSING_CURLY_BRACKET = '}';
    private static final int CHAR_COMMA = ',';
    private static final int CHAR_COLON = ':';
    private static final int CHAR_DOT = '.';
    private static final int CHAR_PLUS = '+';
    private static final int CHAR_E_LOW = 'e';
    private static final int CHAR_E_UP = 'E';
    private static final int CHAR_BACK_SLASH = '\\';


    private Reader reader = null;

    JSONParser(Reader reader) {
        this.reader = reader;
    }

    JSONElement jsonFactory() throws IOException {
        int c;
        while ((c = reader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                if (isDigit(c) || c == CHAR_MINUS) {
                    return numberSeparator(c);
                } else {
                    switch (c) {
                        case DOUBLE_QUOTE:
                            return stringSeparator(c);
                        case FIRST_CHAR_TRUE:
                        case FIRST_CHAR_FALSE:
                            return booleanSeparator(c);
                        case FIRST_CHAR_NULL:
                            return nullSeparator(c);
                        case OPENING_SQUARE_BRACKET:
                            return arraySeparator();
                        case OPENING_CURLY_BRACKET:
                            return objectSeparator();
                        default:
                            throw new IllegalArgumentException("error! JSONParser.jsonFactory");
                    }
                }
            }
        }
        throw new IllegalArgumentException("error! JSONParser.jsonFactory");
    }

    private JSONElement numberSeparator(int indexCharInput) throws IOException {
        StringBuilder toValues = new StringBuilder();
        toValues.append((char) indexCharInput);
        reader.mark(1);
        int c;
        while ((c = reader.read()) != END_READ_STREAM) {
            if (isDigit((char) c) || c == CHAR_MINUS || c == CHAR_DOT || c == CHAR_PLUS || c == CHAR_E_LOW || c == CHAR_E_UP)
            {
                toValues.append((char) c);
                reader.mark(1);
            } else {
                reader.reset();
                return numberCheck(toValues);
            }
        }
        return numberCheck(toValues);
    }

    private JSONElement numberCheck(StringBuilder toValues) {
        if (isNumber(toValues.toString())) {
            return new JSONPrimitive(getAsNumber(toValues.toString()));
        } else {
            throw new IllegalArgumentException("error! JSONParser.numberCheck");
        }
    }

    private Number getAsNumber(String value) {
        return new LazilyParsedNumber(value);
    }

    private JSONElement stringSeparator(int indexCharInput) throws IOException {
        StringBuilder toValues = new StringBuilder();
        boolean tmpIsSlash = false;
        if (indexCharInput == -1) {
            checkString();
        }
        int c;
        while ((c = reader.read()) != END_READ_STREAM) {
            if (c == DOUBLE_QUOTE)
            {
                if (tmpIsSlash) {
                    toValues.deleteCharAt(toValues.length() - 1);
                    toValues.append((char) c);
                    tmpIsSlash = false;
                    continue;
                }
                return new JSONPrimitive(toValues.toString());
            } else {
                toValues.append((char) c);
                tmpIsSlash = c == CHAR_BACK_SLASH;
            }
        }
        throw new IllegalArgumentException("error! JSONParser.stringSeparator");
    }

    private void checkString() throws IOException {
        int c;
        while ((c = reader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                if (c == DOUBLE_QUOTE) {
                    return;
                } else {
                    throw new IllegalArgumentException("error! JSONParser.checkString");
                }
            }
        }
        throw new IllegalArgumentException("error! JSONParser.checkString");
    }

    private JSONElement booleanSeparator(int indexCharInput) throws IOException {
        return new JSONPrimitive(getBooleanOrNullByReader(indexCharInput));
    }

    private Boolean getBooleanOrNullByReader(int c) throws IOException {
        if (c == FIRST_CHAR_TRUE && isCanReadValue("true")) {
            return true;
        } else if (c == FIRST_CHAR_FALSE && isCanReadValue("false")) {
            return false;
        } else if (c == FIRST_CHAR_NULL && isCanReadValue("null"))
            return null;
        throw new IllegalArgumentException();
    }

    private boolean isCanReadValue(String expr) throws IOException {
        int expectedLength = expr.length();
        int index = 1;
        int c;
        while ((c = reader.read()) != END_READ_STREAM) {
            if (!isExpected(c, expr, index++)) {
                throw new IllegalArgumentException();
            }
            if (expectedLength == index) {
                return true;
            }
        }
        if (expectedLength != index) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    private boolean isExpected(int c, String expected, int index) {
        return c == expected.charAt(index);
    }

    private JSONElement nullSeparator(int indexCharInput) throws IOException {
        if (getBooleanOrNullByReader(indexCharInput) != null)
            throw new IllegalArgumentException();
        return JSONNull.INSTANCE;
    }

    private JSONElement arraySeparator() throws IOException {
        JSONArray arrayElement = doBegin();
        if (arrayElement.size() != 0) {
            doValue(arrayElement);
        }
        return arrayElement;
    }

    private JSONArray doBegin() throws IOException {
        JSONArray jsonArray = new JSONArray();
        reader.mark(1);
        int c;
        while ((c = reader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                if (c == CLOSING_SQUARE_BRACKET)
                {
                    return jsonArray;
                } else {
                    reader.reset();
                    jsonArray.add(jsonFactory());
                    return jsonArray;
                }
            }
            reader.mark(1);
        }
        throw new IllegalArgumentException("error! JSONParser.arraySeparator.doBegin");
    }

    private void doValue(JSONArray jsonArray) throws IOException {
        int c;
        while ((c = reader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                if (c == CHAR_COMMA)
                {
                    jsonArray.add(jsonFactory());
                } else {
                    if (c == CLOSING_SQUARE_BRACKET)
                    {
                        return;
                    } else {
                        throw new IllegalArgumentException("error! JSONParser.arraySeparator.doValue");
                    }
                }
            }
        }
        throw new IllegalArgumentException("error! JSONParser.arraySeparator.doValue");
    }

    private JSONElement objectSeparator() throws IOException {
        JSONObject jsonObject = new JSONObject();
        String tmpString = null;
        JSONElement tmpElement = null;
        EnumSeparator currentAction = EnumSeparator.BEGIN;
        int c;
        while ((c = reader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                switch (currentAction) {
                    case BEGIN:
                        if (c == CLOSING_CURLY_BRACKET)
                        {
                            return jsonObject;
                        } else {
                            if (c == DOUBLE_QUOTE) {
                                tmpString = stringSeparator(c).getAsString();
                                currentAction = EnumSeparator.STRING;
                            } else
                                throw new IllegalArgumentException("error! JSONParser.objectSeparator");
                        }
                        break;
                    case STRING:
                        if (c == CHAR_COLON)
                        {
                            tmpElement = jsonFactory();
                            currentAction = EnumSeparator.VALUE;
                        } else {
                            throw new IllegalArgumentException("error! JSONParser.objectSeparator");
                        }
                        break;
                    case VALUE:
                        if (c == CHAR_COMMA)
                        {
                            jsonObject.add(tmpString, tmpElement);
                            tmpString = stringSeparator(-1).getAsString();
                            currentAction = EnumSeparator.STRING;
                        } else {
                            if (c == CLOSING_CURLY_BRACKET)
                            {
                                jsonObject.add(tmpString, tmpElement);
                                return jsonObject;
                            } else {
                                throw new IllegalArgumentException("error! JSONParser.objectSeparator");
                            }
                        }
                        break;
                }
            }
        }
        throw new IllegalArgumentException("error! JSONParser.objectSeparator");
    }
}
