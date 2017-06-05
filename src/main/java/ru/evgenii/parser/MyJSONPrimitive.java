package ru.evgenii.parser;

public class MyJSONPrimitive extends JSONPrimitive {
    public MyJSONPrimitive(String argString) {
        super(argString);
    }
    MyJSONPrimitive(Number argNmber) {
        super(argNmber);
    }
    MyJSONPrimitive(Boolean argNumber) {
        super(argNumber);
    }

    public Object getAsObject() {
        return value;
    }
}
