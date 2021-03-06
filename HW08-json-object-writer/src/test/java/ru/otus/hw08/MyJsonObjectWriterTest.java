package ru.otus.hw08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.hw08.examples.ClassWithCollections;
import ru.otus.hw08.examples.ClassWithComplexCollections;
import ru.otus.hw08.examples.ClassWithObject;
import ru.otus.hw08.examples.PrimitiveClass;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MyJsonObjectWriterTest {

    private static Gson gson;
    private static MyJsonObjectWriter myJsonObjectWriter;

    @BeforeAll
    static void initClass() {
        gson = new Gson();
        myJsonObjectWriter = new MyJsonObjectWriter();
    }

    @Test
    void primitiveToJsonTest() {
        PrimitiveClass obj = new PrimitiveClass(999, "str");

        String json = myJsonObjectWriter.toJson(obj);

        PrimitiveClass obj2 = gson.fromJson(json, PrimitiveClass.class);
        assertEquals(obj, obj2);
    }

    @Test
    void withObjectJsonTest() {
        ClassWithObject obj = new ClassWithObject(2222, new PrimitiveClass(999, "str"));

        String json = myJsonObjectWriter.toJson(obj);

        ClassWithObject obj2 = gson.fromJson(json, ClassWithObject.class);
        assertEquals(obj, obj2);
    }

    @Test
    void emptyOrNulCollectionTest() {
        var list = new ArrayList<String>();
        list.add(null);

        var array = new Integer[]{
                0, null, 2, 3
        };

        var intMap = new HashMap<Integer, Integer>();
        intMap.put(999, null);
        intMap.put(0, 0);

        var obj = new ClassWithCollections(list, null, intMap, array, null, null);

        String json = myJsonObjectWriter.toJson(obj);

        var obj2 = gson.fromJson(json, ClassWithCollections.class);

        assertArrayEquals(obj.getArray(), obj2.getArray());
        assertArrayEquals(obj.getArrayTwo(), obj2.getArrayTwo());
        assertEquals(obj.getList(), obj2.getList());
        assertEquals(obj.getMap(), obj2.getMap());
        assertEquals(obj.getIntMap(), obj2.getIntMap());
        assertEquals(obj.getSet(), obj2.getSet());
    }

    @Test
    void collectionsTest() {
        var list = new ArrayList<String>();
        list.add("hello");
        list.add("world");

        var map = new HashMap<String, Integer>();
        map.put("Russia", 1);
        map.put("NoRussia", 0);

        var intMap = new HashMap<Integer, Integer>();
        intMap.put(999, 1);
        intMap.put(0, 0);

        var array = new Integer[]{
                0, 1, 2, 3
        };

        var arrayTwo = new int[]{
                2, 3, 4, 5
        };

        var set = new HashSet<Integer>();
        set.add(9);
        set.add(1);

        var obj = new ClassWithCollections(list, map, intMap, array, arrayTwo, set);

        String json = myJsonObjectWriter.toJson(obj);

        var obj2 = gson.fromJson(json, ClassWithCollections.class);

        assertArrayEquals(obj.getArray(), obj2.getArray());
        assertArrayEquals(obj.getArrayTwo(), obj2.getArrayTwo());
        assertEquals(obj.getList(), obj2.getList());
        assertEquals(obj.getMap(), obj2.getMap());
        assertEquals(obj.getIntMap(), obj2.getIntMap());
        assertEquals(obj.getSet(), obj2.getSet());

    }

    @Test
    void complexCollectionsTest() {
        var list = new ArrayList<PrimitiveClass>();
        list.add(new PrimitiveClass(21, "to"));
        list.add(new PrimitiveClass(32, "tt"));

        var map = new HashMap<String, PrimitiveClass>();
        map.put("Russia", new PrimitiveClass(11, "el"));
        map.put("NoRussia", new PrimitiveClass(12, "tw"));

        var array = new PrimitiveClass[]{
                new PrimitiveClass(45, "ff"),
                new PrimitiveClass(65, "sf")
        };

        var obj = new ClassWithComplexCollections(array, list, map);

        String json = myJsonObjectWriter.toJson(obj);

        var obj2 = gson.fromJson(json, ClassWithComplexCollections.class);

        assertArrayEquals(obj.getPrimitiveClasses(), obj2.getPrimitiveClasses());
        assertEquals(obj.getPrimitiveClassList(), obj2.getPrimitiveClassList());
        assertEquals(obj.getPrimitiveClassMap(), obj2.getPrimitiveClassMap());
    }

    @Test
    void nonObjectTests() {
        assertEquals(gson.toJson(null), myJsonObjectWriter.toJson(null));
        assertEquals(gson.toJson((byte) 1), myJsonObjectWriter.toJson((byte) 1));
        assertEquals(gson.toJson((short) 1f), myJsonObjectWriter.toJson((short) 1f));
        assertEquals(gson.toJson(1), myJsonObjectWriter.toJson(1));
        assertEquals(gson.toJson(1L), myJsonObjectWriter.toJson(1L));
        assertEquals(gson.toJson(1f), myJsonObjectWriter.toJson(1f));
        assertEquals(gson.toJson(1d), myJsonObjectWriter.toJson(1d));
        assertEquals(gson.toJson("aaa"), myJsonObjectWriter.toJson("aaa"));
        assertEquals(gson.toJson('a'), myJsonObjectWriter.toJson('a'));
        assertEquals(gson.toJson(new int[]{1, 2, 3}), myJsonObjectWriter.toJson(new int[]{1, 2, 3}));
        assertEquals(gson.toJson(List.of(1, 2, 3)), myJsonObjectWriter.toJson(List.of(1, 2, 3)));
        assertEquals(gson.toJson(Collections.singletonList(1)),
                myJsonObjectWriter.toJson(Collections.singletonList(1)));
    }
}