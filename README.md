[![Build Status](https://travis-ci.com/henbh7/flattenxson.svg?branch=master)](https://travis-ci.com/henbh7/flattenxson)

FlattenXson
---------

FlattenXson provides flatten and unflatten utils for json and bson documents.

Features:

* Choose the seperator char for seperate document levels.
* Max document levels to flat.
* Choose unsupported types by types list.

here's some example code:

```java
String bsonString = "{\n" +
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
            Document bson = Document.parse(bsonString);
            Document flattenDoc = BsonFlatten.flattenDoc(bson);
            Document unflattenDoc = BsonFlatten.unFlatten(flattenDoc.toJson());
        } catch (FlattenXsonException e) {
            e.printStackTrace();
        }
```


