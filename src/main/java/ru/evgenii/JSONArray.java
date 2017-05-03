package ru.evgenii;


import java.util.*;

/**
 * Created by e.kostyukovskiy on 03.04.2017.
 */
public class JSONArray extends JSONElement {

    private   List<JSONElement> elements;
    public JSONArray() {
        elements = new ArrayList<JSONElement>();
    }
    public int size() {
        return elements.size();
    }

    public JSONElement get(int i) {
        return elements.get(i);
    }

    /*public <T> Spliterator<T> spliterator() {
        Spliterators.spliterator(T);
        //return null;
       //Arrays.spliterator(src);
    }*/
    /*public <T> Spliterator<T> spliterator() {
        return elements.spliterator();
        //return null;
        //Arrays.spliterator(src);
    }*/


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

    public <JSONElement> Spliterator<ru.evgenii.JSONElement> spliterator() {
        return elements.spliterator();
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof JSONArray && ((JSONArray) o).elements.equals(elements));
    }
}
