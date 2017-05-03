package ru.evgenii;

import com.google.gson.internal.LazilyParsedNumber;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import static java.lang.Character.isDigit;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;

/**
 * Created by e.kostyukovskiy on 24.04.2017.
 */
public class JSONParser {

    private Reader stringReader = null;

    public JSONParser(Reader stringReader) {
        this.stringReader = stringReader;
    }

    public JSONElement jsonFactory() throws IOException {
        int c;
        while ((c = stringReader.read()) != -1) {
            if (!Character.isWhitespace(c)) {
                if (isDigit((char) c) || c == 45) //-
                {
                    return numberSeparator(c);
                } else {
                    switch (c) {
                        case 34://"
                            return stringSeparator(c);
                        case 116: //t
                        case 102: //f
                            return booleanSeparator(c);
                        case 110: //n
                            return nullSeparator(c);
                        case 91: //[
                            return arraySeparator(c);
                        case 123: //{
                            return objectSeparator(c);
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
        stringReader.mark(1);
        int c;
        while ((c = stringReader.read()) != -1) {
            if (isDigit((char) c) || c == 45 || c == 46 || c == 43 || c == 69 || c == 101) //- . + e E
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

            /*Number ff = new Number() {
                @Override
                public int intValue() {
                    return  toValues.toString();
                }

                @Override
                public long longValue() {
                    return 0;
                }

                @Override
                public float floatValue() {
                    return 0;
                }

                @Override
                public double doubleValue() {
                    return 0;
                }
            }*/
            return new JSONPrimitive(getAsNumber(toValues.toString()));
            //Values.add(new Pair(Number.class, toValues.toString()));
            //toValues.setLength(0);
        } else {
            throw new IllegalArgumentException("error! JSONParser.numberCheck");
        }
    }

    private Number getAsNumber(String value) {
        return new LazilyParsedNumber((String) value);
    }

    private JSONElement stringSeparator(int indexCharInput) throws IOException {
        StringBuilder toValues = new StringBuilder();
        boolean tmpIsSlash = false;
        if (indexCharInput == -1) {
            indexCharInput = checkString();
        }
        int c;
        while ((c = stringReader.read()) != -1) {
            /*if (c == 92)//\
            {
                toValues.append((char) c);
                tmpIsSlash = true;
                continue;
            }*/
            if (c == 34)//"
            {
                if(tmpIsSlash)
                {
                    toValues.deleteCharAt(toValues.length()-1);
                    toValues.append((char) c);
                    tmpIsSlash = !tmpIsSlash;
                    continue;
                }
                return new JSONPrimitive(toValues.toString());
            } else {
                toValues.append((char) c);
                if (c == 92)//\
                {
                    tmpIsSlash = true;
                }
                else {
                    tmpIsSlash = false;
                }
            }
        }
        throw new IllegalArgumentException("error! JSONParser.stringSeparator");
    }

    private int checkString() throws IOException {
        int c;
        while ((c = stringReader.read()) != -1) {
            if (!Character.isWhitespace(c)) {
                if (c == 34)//"
                {
                    return c;
                } else {
                    throw new IllegalArgumentException("error! JSONParser.checkString");
                }
            }
        }
        throw new IllegalArgumentException("error! JSONParser.checkString");
    }

    private JSONElement booleanSeparator(int indexCharInput) throws IOException {
        ArrayList<Character> postfixValue = new ArrayList<Character>();
        if (indexCharInput == 't') {
            postfixValue.add('r');
            postfixValue.add('u');
            postfixValue.add('e');
        } else if (indexCharInput == 'f') {
            postfixValue.add('a');
            postfixValue.add('l');
            postfixValue.add('s');
            postfixValue.add('e');
        } else throw new IOException("error! JSONParser.booleanSeparator");
        int initSize = postfixValue.size();
        int c;
        while ((c = stringReader.read()) != -1) {
            try {
                if ((char) c == postfixValue.get(0)) {
                    postfixValue.remove(0);
                    if (postfixValue.size() == 0) {
                        postfixValue = null;
                        if (initSize == 4)
                            return new JSONPrimitive(false);
                        else
                            return new JSONPrimitive(true);
                    }
                } else {
                    throw new IllegalArgumentException("error bool");
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException("error bool");
            }
        }
        throw new IllegalArgumentException("error! JSONParser.booleanSeparator");
    }

    private JSONElement nullSeparator(int indexCharInput) throws IOException {
        ArrayList<Character> postfixValue = new ArrayList<Character>();

        if (indexCharInput == 'n') {
            postfixValue.add('u');
            postfixValue.add('l');
            postfixValue.add('l');
        } else throw new IOException("error! JSONParser.nullSeparator");

        int c;
        while ((c = stringReader.read()) != -1) {
            try {
                if ((char) c == postfixValue.get(0)) {
                    postfixValue.remove(0);
                    if (postfixValue.size() == 0) {
                        return JSONNull.INSTANCE;
                    }
                } else {
                    throw new IllegalArgumentException("error! JSONParser.nullSeparator");
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException("error! JSONParser.nullSeparator");
            }
        }
        throw new IllegalArgumentException("error! JSONParser.nullSeparator");
    }

    private JSONElement arraySeparator(int indexCharInput) throws IOException {
        JSONArray arrayElement = null;
        arrayElement = doBegin();
        if (arrayElement.size() != 0) {
            doValue(arrayElement);
        }
        return arrayElement;
    }

    private JSONArray doBegin() throws IOException {
        JSONArray jsonArray = new JSONArray();
        stringReader.mark(1);
        int c;
        while ((c = stringReader.read()) != -1) {
            if (!Character.isWhitespace(c)) {
                if (c == 93)//]
                {
                    return jsonArray;
                } else {
                    stringReader.reset();
                    jsonArray.add(jsonFactory());
                    return jsonArray;
                }
            }
            stringReader.mark(1);
        }
        throw new IllegalArgumentException("error! JSONParser.arraySeparator.doBegin");
    }

    private void doValue(JSONArray jsonArray) throws IOException {
        int c;
        while ((c = stringReader.read()) != -1) {
            if (!Character.isWhitespace(c)) {
                if (c == 44)//,
                {
                    jsonArray.add(jsonFactory());
                } else {
                    if (c == 93)//]
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

    private JSONElement objectSeparator(int indexCharInput) throws IOException {
        //ValueSeparator valueSeparator = null;
        JSONObject jsonObject = new JSONObject();
        String tmpString = null;
        JSONElement tmpElement = null;
        EnumSeparator currentAction = EnumSeparator.BEGIN;
        int c;
        while ((c = stringReader.read()) != -1) {
            if (!Character.isWhitespace(c)) {
                switch (currentAction) {
                    case BEGIN:
                        if (c == 125)//}
                        {
                            return jsonObject;
                        } else {
                            if (c == 34)//"
                            {
                                tmpString = stringSeparator(c).getAsString();
                                currentAction = EnumSeparator.STRING;
                            } else
                                throw new IllegalArgumentException("error! JSONParser.objectSeparator");
                        }
                        break;
                    case STRING:
                        if (c == 58) //:
                        {
                            tmpElement = jsonFactory();
                            currentAction = EnumSeparator.VALUE;
                        } else {
                            throw new IllegalArgumentException("error! JSONParser.objectSeparator");
                        }
                        break;
                    case VALUE:
                        if (c == 44)//,
                        {
                            jsonObject.add(tmpString, tmpElement);
                            tmpString = stringSeparator(-1).getAsString();
                            currentAction = EnumSeparator.STRING;
                        } else {
                            if (c == 125)//}
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
