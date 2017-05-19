package ru.evgenii;

import com.google.gson.internal.LazilyParsedNumber;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static java.lang.Character.isDigit;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;

public class ImplementedJsonParser implements StreamingJsonParser {

    private static final int END_READ_STREAM = -1;
    private static final int CHAR_MINUS = '-';
    private static final int CHAR_DOUBLE_QUOTE = '"';
    private static final int CHAR_FIRST_CHAR_TRUE = 't';
    private static final int CHAR_FIRST_CHAR_FALSE = 'f';
    private static final int CHAR_FIRST_CHAR_NULL = 'n';
    private static final int CHAR_OPENING_SQUARE_BRACKET = '[';
    private static final int CHAR_CLOSING_SQUARE_BRACKET = ']';
    private static final int CHAR_OPENING_CURLY_BRACKET = '{';
    private static final int CHAR_CLOSING_CURLY_BRACKET = '}';
    private static final int CHAR_COMMA = ',';
    private static final int CHAR_COLON = ':';
    private static final int CHAR_DOT = '.';
    private static final int CHAR_PLUS = '+';
    private static final int CHAR_E_LOW = 'e';
    private static final int CHAR_E_UP = 'E';
    private static final int CHAR_BACK_SLASH = '\\';

    private StringReader stringReader = null;

    @Override
    public JSONElement parse(Reader reader)  {
        stringReader = (StringReader)reader;
        try(StringReader tmpStringReader = stringReader)
        {
            JSONElement retJson = getJSONValue();
            int c;
            while ((c = tmpStringReader.read()) != -1) {
                if (!Character.isWhitespace(c)) {
                    throw new IllegalArgumentException("error! ImplementedJsonParser.parse");
                }
            }
            return retJson;
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private JSONElement getJSONValue() throws IOException {
        int c;
        while ((c = stringReader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                if (isDigit(c) || c == CHAR_MINUS) {
                    return getJSONNumber(c);
                } else {
                    switch (c) {
                        case CHAR_DOUBLE_QUOTE:
                            return getJSONString(c);
                        case CHAR_FIRST_CHAR_TRUE:
                        case CHAR_FIRST_CHAR_FALSE:
                            return getJSONBoolean(c);
                        case CHAR_FIRST_CHAR_NULL:
                            return getJSONNull(c);
                        case CHAR_OPENING_SQUARE_BRACKET:
                            return getJSONArray();
                        case CHAR_OPENING_CURLY_BRACKET:
                            return getJSONObject();
                        default:
                            throw new IllegalArgumentException("error! ImplementedJsonParser.getJSONValue");
                    }
                }
            }
        }
        throw new IllegalArgumentException("error! ImplementedJsonParser.getJSONValue");
    }
    private JSONElement getJSONNumber(int indexCharInput) throws IOException {
        StringBuilder toValues = new StringBuilder();
        toValues.append((char) indexCharInput);
        stringReader.mark(1);
        int c;
        while ((c = stringReader.read()) != END_READ_STREAM) {
            if (isDigit((char) c) || c == CHAR_MINUS || c == CHAR_DOT || c == CHAR_PLUS || c == CHAR_E_LOW || c == CHAR_E_UP)
            {
                toValues.append((char) c);
                stringReader.mark(1);
            } else {
                stringReader.reset();
                return numberCheck(toValues);
            }
        }
        return numberCheck(toValues);
    }

    private JSONElement numberCheck(StringBuilder toValues) {
        if (isNumber(toValues.toString())) {
            return new JSONPrimitive(getAsNumber(toValues.toString()));
        } else {
            throw new IllegalArgumentException("error! ImplementedJsonParser.numberCheck");
        }
    }

    private Number getAsNumber(String value) {
        return new LazilyParsedNumber(value);
    }

    private JSONElement getJSONString(int indexCharInput) throws IOException {
        StringBuilder toValues = new StringBuilder();
        boolean tmpIsSlash = false;
        if (indexCharInput == -1) {
            checkString();
        }
        int c;
        while ((c = stringReader.read()) != END_READ_STREAM) {
            if (c == CHAR_DOUBLE_QUOTE)
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
        throw new IllegalArgumentException("error! ImplementedJsonParser.getJSONString");
    }

    private void checkString() throws IOException {
        int c;
        while ((c = stringReader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                if (c == CHAR_DOUBLE_QUOTE) {
                    return;
                } else {
                    throw new IllegalArgumentException("error! ImplementedJsonParser.checkString");
                }
            }
        }
        throw new IllegalArgumentException("error! ImplementedJsonParser.checkString");
    }
    private JSONElement getJSONBoolean(int indexCharInput) throws IOException {
        return new JSONPrimitive(getBooleanOrNullByReader(indexCharInput));
    }
    private Boolean getBooleanOrNullByReader(int c) throws IOException {
        if (c == CHAR_FIRST_CHAR_TRUE && isCanReadValue("true")) {
            return true;
        } else if (c == CHAR_FIRST_CHAR_FALSE && isCanReadValue("false")) {
            return false;
        } else if (c == CHAR_FIRST_CHAR_NULL && isCanReadValue("null"))
            return null;
        throw new IllegalArgumentException();
    }

    private boolean isCanReadValue(String expr) throws IOException {
        int expectedLength = expr.length();
        int index = 1;
        int c;
        while ((c = stringReader.read()) != END_READ_STREAM) {
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

    private JSONElement getJSONNull(int indexCharInput) throws IOException {
        if (getBooleanOrNullByReader(indexCharInput) != null)
            throw new IllegalArgumentException();
        return JSONNull.INSTANCE;
    }

    private JSONElement getJSONArray() throws IOException {
        JSONArray arrayElement = doBegin();
        if (arrayElement.size() != 0) {
            doValue(arrayElement);
        }
        return arrayElement;
    }

    private JSONArray doBegin() throws IOException {
        JSONArray jsonArray = new JSONArray();
        stringReader.mark(1);
        int c;
        while ((c = stringReader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                if (c == CHAR_CLOSING_SQUARE_BRACKET)
                {
                    return jsonArray;
                } else {
                    stringReader.reset();
                    jsonArray.add(getJSONValue());
                    return jsonArray;
                }
            }
            stringReader.mark(1);
        }
        throw new IllegalArgumentException("error! ImplementedJsonParser.doBegin");
    }

    private void doValue(JSONArray jsonArray) throws IOException {
        int c;
        while ((c = stringReader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                if (c == CHAR_COMMA)
                {
                    jsonArray.add(getJSONValue());
                } else {
                    if (c == CHAR_CLOSING_SQUARE_BRACKET)
                    {
                        return;
                    } else {
                        throw new IllegalArgumentException("error! ImplementedJsonParser.doValue");
                    }
                }
            }
        }
        throw new IllegalArgumentException("error! ImplementedJsonParser.doValue");
    }

    private JSONElement getJSONObject() throws IOException {
        JSONObject jsonObject = new JSONObject();
        String tmpString = null;
        JSONElement tmpElement = null;
        EnumSeparator currentAction = EnumSeparator.BEGIN;
        int c;
        while ((c = stringReader.read()) != END_READ_STREAM) {
            if (!Character.isWhitespace(c)) {
                switch (currentAction) {
                    case BEGIN:
                        if (c == CHAR_CLOSING_CURLY_BRACKET)
                        {
                            return jsonObject;
                        } else {
                            if (c == CHAR_DOUBLE_QUOTE) {
                                tmpString = getJSONString(c).getAsString();
                                currentAction = EnumSeparator.STRING;
                            } else
                                throw new IllegalArgumentException("error! ImplementedJsonParser.getJSONObject");
                        }
                        break;
                    case STRING:
                        if (c == CHAR_COLON)
                        {
                            tmpElement = getJSONValue();
                            currentAction = EnumSeparator.VALUE;
                        } else {
                            throw new IllegalArgumentException("error! ImplementedJsonParser.getJSONObject");
                        }
                        break;
                    case VALUE:
                        if (c == CHAR_COMMA)
                        {
                            jsonObject.add(tmpString, tmpElement);
                            tmpString = getJSONString(-1).getAsString();
                            currentAction = EnumSeparator.STRING;
                        } else {
                            if (c == CHAR_CLOSING_CURLY_BRACKET)
                            {
                                jsonObject.add(tmpString, tmpElement);
                                return jsonObject;
                            } else {
                                throw new IllegalArgumentException("error! ImplementedJsonParser.getJSONObject");
                            }
                        }
                        break;
                }
            }
        }
        throw new IllegalArgumentException("error! ImplementedJsonParser.getJSONObject");
    }
}
