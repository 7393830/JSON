package ru.evgenii.parser;


import java.util.*;

public class JSONArray extends JSONElement {

    private List<JSONElement> elements;
    JSONArray() {
        elements = new ArrayList<>();
    }
    public int size() {
        return elements.size();
    }

    public JSONElement get(int i) {
        return elements.get(i);
    }


    public void add(JSONElement element) {
        if (element == null) {
            element = JSONNull.INSTANCE;
        }
        elements.add(element);
    }
    @Override
    public int getAsInt() {
        if (elements.size() == 1) {
            return elements.get(0).getAsInt();
        }
        throw new IllegalStateException();
    }

    public Spliterator<JSONElement> spliterator() {
        return elements.spliterator();
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof JSONArray && ((JSONArray) o).elements.equals(elements));
    }
}
