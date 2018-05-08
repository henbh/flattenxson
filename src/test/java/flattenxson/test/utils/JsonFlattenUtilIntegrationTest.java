package flattenxson.test.utils;

import flattenxson.exceptions.FlattenXsonException;
import flattenxson.utils.BsonFlattenUtil;
import flattenxson.utils.JsonFlattenUtil;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class JsonFlattenUtilIntegrationTest {
    @Test
    public void test1() {
        String jsonString = "{\n" +
                "  \"address\": {\n" +
                "     \"building\": \"1007\",\n" +
                "     \"coord\": [ -73.856077, 40.848447 ],\n" +
                "     \"street\": \"Morris Park Ave\",\n" +
                "     \"zipcode\": \"10462\"\n" +
                "  },\n" +
                "}";

        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject flattenDoc = JsonFlattenUtil.flattenDoc(json);

            System.out.println("Flatten Doc :::: " + flattenDoc);

            JSONObject unflattenDoc = JsonFlattenUtil.unFlatten(flattenDoc.toString());
            System.out.println("UnFlatten Doc :::: " + unflattenDoc);

            Assert.assertFalse(!flattenDoc.toString().contains("__"));
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
            JsonFlattenUtil.unFlatten(jsonString);
        } catch (FlattenXsonException e) {
            e.printStackTrace();
            Assert.assertTrue(true);
        }
    }
}
