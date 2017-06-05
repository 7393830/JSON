package ru.evgenii;

class MyJSONPrimitive extends JSONPrimitive {
    MyJSONPrimitive(String test) {
        super(test);
    }
    MyJSONPrimitive(Number test) {
        super(test);
    }
    MyJSONPrimitive(Boolean test) {
        super(test);
    }


    Object getAsObject() {
        return value;
    }
}
