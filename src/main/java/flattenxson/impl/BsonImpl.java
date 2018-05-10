package flattenxson.impl;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import flattenxson.exceptions.FlattenException;
import flattenxson.exceptions.FlattenXsonException;
import flattenxson.exceptions.UnflattenException;
import org.bson.Document;

import java.util.*;

public class BsonImpl implements BaseFlatten {

    public Document flatten(Object doc, String separatorChar, int maxDocLevelsSupported, List<Class<?>> unSupportedTypes) throws FlattenXsonException {
        Map<String, Object> newDoc = new HashMap<>();
        try {
            flattenRecursive(null, doc, newDoc, separatorChar, maxDocLevelsSupported, unSupportedTypes);
        } catch (Exception e) {
            throw new FlattenException(
                    String.format("An error occurred while trying to flat the following doc: %s", doc), e);
        }

        return new Document(newDoc);
    }


    public void flattenRecursive(String rootName,
                                 Object doc,
                                 Map<String, Object> newDoc,
                                 String separatorChar,
                                 int maxDocLevelsSupported,
                                 List<Class<?>> unSupportedTypes) {
        if (doc instanceof Document) {
            Document document = (Document) doc;
            Set<Map.Entry<String, Object>> documentFields = document.entrySet();

            if (documentFields == null) return;

            documentFields.forEach(field -> {
                String key = validateFieldName(field.getKey(), separatorChar);

                if (rootName != null) {
                    key = rootName + separatorChar + key;
                }

                if (isFieldDocDepthValid(key, maxDocLevelsSupported, separatorChar)) {

                    Object value = field.getValue();

                    if (value != null) {
                        if (isTypeValid(value.getClass(), unSupportedTypes)) {
                            if (value instanceof Document) {
                                flattenRecursive(key, value, newDoc, separatorChar, maxDocLevelsSupported, unSupportedTypes);
                            } else if (value instanceof ArrayList) {
                                flatArrayObject(key, (ArrayList) value, newDoc, separatorChar, maxDocLevelsSupported, unSupportedTypes);
                            } else {
                                newDoc.put(key, value);
                                flattenRecursive(key, value, newDoc, separatorChar, maxDocLevelsSupported, unSupportedTypes);
                            }
                        }
                    }
                }
            });
        }
    }

    public Document unFlatten(String doc, String separatorChar) throws UnflattenException {
        Document result = null;

        try {
            DBObject bsonDoc = (DBObject) JSON.parse(doc);
            HashMap newDoc = unFlattenHelper(bsonDoc.keySet(), separatorChar, bsonDoc);
            result = new Document(newDoc);
        } catch (Exception e) {
            throw new UnflattenException(
                    String.format("An error occurred while trying to unflat the following doc: %s", doc), e);
        }

        return result;
    }
}

