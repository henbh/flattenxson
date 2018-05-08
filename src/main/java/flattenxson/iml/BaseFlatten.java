package flattenxson.iml;

import com.mongodb.DBObject;
import flattenxson.exceptions.FlattenException;
import flattenxson.exceptions.FlattenXsonException;
import org.json.JSONObject;

import java.util.*;

public interface BaseFlatten {
    Object flatten(Object doc, String separatorChar, int maxDocLevelsSupported, List<Class<?>> unSupportedTypes) throws FlattenXsonException;

    Object unFlatten(String doc, String separatorChar) throws FlattenXsonException;

    default void unFlattenRecursive(LinkedList splitKey, Object value, HashMap newDoc) {

        if (splitKey.size() == 1) {
            newDoc.put(String.valueOf(splitKey.get(0)), value);
            return;
        }

        String key = String.valueOf(splitKey.get(0));
        splitKey.removeFirst();

        if (newDoc.get(key) != null) {
            unFlattenRecursive(splitKey, value, (HashMap) newDoc.get(key));
        } else {
            HashMap newField = new HashMap();
            newDoc.put(key, newField);
            unFlattenRecursive(splitKey, value, (HashMap) newDoc.get(key));
        }
    }

    default String validateFieldName(String fieldName, String separatorChar) {
        fieldName = fieldName.replace(separatorChar, "");

        return fieldName;
    }

    default Boolean isTypeValid(Class<?> type, List<Class<?>> unSupportedTypes) {
        if (unSupportedTypes != null) {
            Set<Class<?>> unSupportedValues = new HashSet<>(unSupportedTypes);
            if (unSupportedValues.contains(type)) {
                return false;
            }
        }

        return true;
    }

    default boolean isFieldDocDepthValid(String fieldName, int maxDocLevelsSupported, String separatorChar) {
        boolean result = true;

        if (fieldName.split("\\" + separatorChar).length > maxDocLevelsSupported) {
            result = false;
        }

        if (maxDocLevelsSupported == 0) {
            result = true;
        }

        return result;
    }

    default HashMap unFlattenHelper(Set<String> docKeys, String separatorChar, Object doc) {
        HashMap newDoc = new HashMap();

        docKeys.forEach(key -> {
            LinkedList splitKey = new LinkedList(Arrays.asList(key.split(separatorChar)));
            if (splitKey.size() > 0) {
                if (doc instanceof DBObject) {
                    unFlattenRecursive(splitKey, ((DBObject) doc).get(key), newDoc);
                } else if (doc instanceof JSONObject) {
                    unFlattenRecursive(splitKey, ((JSONObject) doc).get(key), newDoc);
                }
            } else {
                if (doc instanceof DBObject) {
                    newDoc.put(key, ((DBObject) doc).get(key));
                } else if (doc instanceof JSONObject) {
                    newDoc.put(key, ((JSONObject) doc).get(key));
                }
            }
        });

        return newDoc;
    }
}
