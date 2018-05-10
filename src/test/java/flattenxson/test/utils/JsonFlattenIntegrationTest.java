package flattenxson.test.utils;

import flattenxson.exceptions.FlattenXsonException;
import flattenxson.modules.JsonFlatten;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class JsonFlattenIntegrationTest {
    @Test
    public void test1() {
        String jsonString = "{\n" +
                "  \"address\": {\n" +
                "     \"building\": \"1007\",\n" +
                "     \"coord\": [ -73.856077, 40.848447 ],\n" +
                "     \"street\": \"Morris Park Ave\",\n" +
                "     \"zipcode\": \"10462\"\n" +
                "  },\n" +
                "  \"borough\": \"Bronx\",\n" +
                "  \"cuisine\": \"Bakery\",\n" +
                "  \"grades\": [\n" +
                "     { \"date\": { \"$date\": 1393804800000 }, \"grade\": \"A\", \"score\": 2 },\n" +
                "     { \"date\": { \"$date\": 1378857600000 }, \"grade\": \"A\", \"score\": 6 },\n" +
                "     { \"date\": { \"$date\": 1358985600000 }, \"grade\": \"A\", \"score\": 10 },\n" +
                "     { \"date\": { \"$date\": 1322006400000 }, \"grade\": \"A\", \"score\": 9 },\n" +
                "     { \"date\": { \"$date\": 1299715200000 }, \"grade\": \"B\", \"score\": 14 }\n" +
                "  ],\n" +
                "  \"name\": \"Morris Park Bake Shop\",\n" +
                "  \"restaurant_id\": \"30075445\"\n" +
                "}";

        try {
            JSONObject json = new JSONObject(jsonString);

            //#1
            JSONObject flattenDoc1 = JsonFlatten.flattenDoc(json);

            //#2
            ArrayList<Class<?>> classArrayList = new ArrayList<>();
            classArrayList.add(ArrayList.class);
            JSONObject flattenDoc2 = JsonFlatten.flattenDoc(json, "**", 1, classArrayList);

            //#3
            JSONObject flattenDoc3 = JsonFlatten.flattenDoc(json, "**", 1);

            System.out.println("Flatten1 Doc :::: " + flattenDoc1);
            System.out.println("Flatten2 Doc :::: " + flattenDoc2);
            System.out.println("Flatten3 Doc :::: " + flattenDoc3);

            JSONObject unflattenDoc = JsonFlatten.unFlatten(flattenDoc1.toString());
            System.out.println("UnFlatten Doc :::: " + unflattenDoc);

            Assert.assertFalse(!flattenDoc1.toString().contains("__"));
            Assert.assertFalse(unflattenDoc.toString().contains("__"));
        } catch (FlattenXsonException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        String jsonString = "{\n" +
                "  \"address\": {\n" +
                "     \"building\": \"1007\",\n" +
                "     \"coord\": [ -73.856077, 40.848447 ],\n" +
                "     \"street\": \"Morris Park Ave\",\n" +
                "     \"zipcode\": \"10462\"\n" +
                "  },\n" +
                "  \"borough\": \"Bronx\",\n" +
                "  \"cuisine\": \"Bakery\",\n" +
                "  \"grades\": [\n";

        try {
            JsonFlatten.unFlatten(jsonString);
        } catch (FlattenXsonException e) {
            e.printStackTrace();
            Assert.assertTrue(true);
        }
    }
}
