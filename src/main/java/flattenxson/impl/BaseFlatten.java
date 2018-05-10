package flattenxson.impl;

import com.mongodb.DBObject;
import flattenxson.exceptions.FlattenXsonException;
import org.json.JSONObject;

import java.util.*;

public interface BaseFlatten {
    Object flatten(Object doc, String separatorChar, int maxDocLevelsSupported, List<Class<?>> unSupportedTypes) throws FlattenXsonException;

    void flattenRecursive(String rootName,
                          Object doc,
                          Map<String, Object> newDoc,
                          String separatorChar,
                          int maxDocLevelsSupported,
                          List<Class<?>> unSupportedTypes);

    Object unFlatten(String doc, String separatorChar) throws FlattenXsonException;

    default void unFlattenRecursive(LinkedList<String> splitKey, Object value, HashMap newDoc) {

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

        if (fieldName.split(buildSeparatorCharRegex(separatorChar)).length > maxDocLevelsSupported) {
            result = false;
        }

        if (maxDocLevelsSupported == 0) {
            result = true;
        }

        return result;
    }

    default String buildSeparatorCharRegex(String separatorChar) {
        StringBuilder result = new StringBuilder();

        for (char item : separatorChar.toCharArray()) {
            result.append("\\");
            result.append(item);
        }

        return result.toString();
    }

    default HashMap unFlattenHelper(Set<String> docKeys, String separatorChar, Object doc) {
        HashMap newDoc = new HashMap();

        docKeys.forEach(key -> {
            LinkedList<String> splitKey = new LinkedList<>(Arrays.asList(key.split(separatorChar)));
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

    default void flatArrayObject(String rootName,
                                 ArrayList arrayList,
                                 Map<String, Object> newDoc,
                                 String separatorChar,
                                 int maxDocLevelsSupported,
                                 List<Class<?>> unSupportedTypes) {
        int index = 0;

        for (Object item : arrayList) {
            if (isTypeValid(item.getClass(), unSupportedTypes)) {
                String fieldName = String.format("%s%s%d", rootName, separatorChar, index);

                if (item instanceof org.bson.Document) {
                    flattenRecursive(fieldName, item, newDoc, separatorChar, maxDocLevelsSupported, unSupportedTypes);
                }
                if (item instanceof HashMap) {
                    flattenRecursive(fieldName, new JSONObject((HashMap)item), newDoc, separatorChar, maxDocLevelsSupported, unSupportedTypes);
                } else {
                    newDoc.put(fieldName, item);
                }

                index++;
            }
        }
    }
}
