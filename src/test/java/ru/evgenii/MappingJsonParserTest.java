package ru.evgenii;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Assert;
import org.junit.Test;
import ru.evgenii.mapper.Mapper;
import ru.evgenii.mapper.MappingJsonParser;
import ru.evgenii.mapper.reflection.ReflectionMapper;
import ru.evgenii.parser.ImplementedJsonParser;
import ru.evgenii.parser.JSONElement;
import ru.evgenii.parser.JSONObject;
import ru.evgenii.parser.MyJSONPrimitive;


public class MappingJsonParserTest {

    private static final ReflectionMapper mapper = new ReflectionMapper();

    private static final ImplementedJsonParser sjp = new ImplementedJsonParser();

    private static final MappingJsonParser mjp =  new MappingJsonParser() {

        @Override
        public <T> T parse(Reader r, Mapper<T> mapper) {
            T result = null;
            try {
                result = mapper.map(sjp.parse(r));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return result;
        }
    };

    @Test
    public void testSimpleMapper1() {
        String json = "\"5\"";
        Reader r = new StringReader(json);
        Mapper<Integer> stringMapper = JSONElement::getAsInt;
        int result  = mjp.parse(r, stringMapper);
        Assert.assertEquals("I shall not crash", 5, result);
    }

    @Test
    public void testSimpleMapper() {
        String json = "\"abcdef\"";
        Reader r = new StringReader(json);
        Mapper<String> stringMapper = JSONElement::getAsString;
        String result  = mjp.parse(r, stringMapper);
        Assert.assertEquals("I shall not crash", "abcdef", result);
    }

    @Test
    public void testMapMapper() {
        String json = "{\"a\":\"abcdef\", \"b\": \"bgedf\"}";
        Reader r = new StringReader(json);
        Mapper<HashMap<String, String>> hashMapMapper = e -> {
            JSONObject jo = e.getAsJsonObject();
            HashMap<String, String> result = new HashMap<>();
            jo
                    .entrySet()
                    .forEach((s) -> result.put(s.getKey(), s.getValue().getAsString()));

            return result;
        };
        HashMap<String, String> result = mjp.parse(r, hashMapMapper);
        Assert.assertEquals("key 'a' bounded correctly", "abcdef", result.get("a"));
        Assert.assertEquals("key 'b' bounded correctly", "bgedf", result.get("b"));
    }
    @Test
    public void testHeterogeneousMapper1() { // Is it even possible?!
        String json = "{\"a\": 6666.99, \"b\": 200, \"с\": 0.2E3}";
        Reader r = new StringReader(json);
 JSONElement jsonElement = sjp.parse(r);
 JSONObject jsonObject = jsonElement.getAsJsonObject();

        Assert.assertEquals("key 'a' bounded correctly", 6666.99F, ((MyJSONPrimitive)jsonObject.get("a")).getAsObject());
        Assert.assertEquals("key 'b' bounded correctly", 200, ((MyJSONPrimitive)jsonObject.get("b")).getAsObject());
    }
    @Test
    public void testHeterogeneousMapper() { // Is it even possible?!
        String json = "{\"a\": \"abcdef\", \"b\": 1234}";
        Reader r = new StringReader(json);
        Mapper<HashMap<String, ?>> hashMapMapper = e -> {
            JSONObject jo = e.getAsJsonObject();
            HashMap<String,Object> result = new HashMap<>();
            jo
                    .entrySet()
                    .forEach((s) -> {
                        //JSONPrimitive jp = (JSONPrimitive)s.getValue();
                        MyJSONPrimitive jp = (MyJSONPrimitive)s.getValue();
                        result.put(s.getKey(), jp.getAsObject());
                    });
            return result;
        };
        HashMap<String, ?> result = mjp.parse(r, hashMapMapper);
        Assert.assertEquals("key 'a' bounded correctly", "abcdef", result.get("a"));
        Assert.assertEquals("key 'b' bounded correctly", 1234, result.get("b"));
    }

    @Test
    public void testPOJOMapper() {
        String json = "{\"name\": \"Connor McLeod\", \"age\": 4412}";
        Human ref = new Human().setAge(4412).setName("Connor McLeod");
        Reader r = new StringReader(json);
        Mapper<Human> someBeanMapper = e -> {
            Human result = new Human();
            JSONObject jo = e.getAsJsonObject();
            return result
                   .setName(jo.get("name").getAsString())
                   .setAge(jo.get("age").getAsLong());
        };
        Human result = mjp.parse(r, someBeanMapper);
        Assert.assertEquals("names match", "Connor McLeod", result.getName());
        Assert.assertEquals("ages match", 4412, result.getAge());
        Assert.assertEquals("we ve got our highlander", ref, result);
    }

    @Test
    public void testHumanEquality() {
        Human h1 = new Human().setAge(123);
        Human h2 = new Human().setAge(123).setName("Notnull Patrick");
        Assert.assertNotEquals("No, they are not", h1, h2);

    }

    @Test
    public void testBrokenHashCode() {
        Human h1 = new Human().setAge(12).setName("NoHashCode Kenny");
        Human h2 = new Human().setAge(12).setName("NoHashCode Kenny");

        Map<Human, String> someInfo = new HashMap<>();
        someInfo.put(h1, "related data");
        Assert.assertEquals("they all created equal", h1, h2);
        Assert.assertEquals("Can we get data we stored?", "related data", someInfo.get(h2));
    }

    private static class Human { //TODO: incomplete.
        private String name;
        private long age;
        public Human() {

        }

        public String getName() {
            return name;
        }

        public Human setName(String name) {
            this.name = name;

            return this;
        }

        public long getAge() {
            return age;
        }

        public Human setAge(long age) {
            this.age = age;

            return this;
        }

        @Override
        public boolean equals(Object obj) {
            return new EqualsBuilder().reflectionEquals(this, obj);
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17,37)
                    .append(getName())
                    .append(getAge())
                    .toHashCode();
        }
    }
}
