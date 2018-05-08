package flattenxson.impl;

import flattenxson.exceptions.FlattenException;
import flattenxson.exceptions.FlattenXsonException;
import flattenxson.exceptions.UnflattenException;
import org.json.JSONObject;

import java.util.*;

public class JsonImpl implements BaseFlatten {

    @Override
    public JSONObject flatten(Object doc, String separatorChar, int maxDocLevelsSupported, List<Class<?>> unSupportedTypes) throws FlattenXsonException {
        Map<String, Object> newDoc = new HashMap<>();
        try {
            flattenRecursive(null, doc, newDoc, separatorChar, maxDocLevelsSupported, unSupportedTypes);
        } catch (Exception e) {
            throw new FlattenException(
                    String.format("An error occurred while trying to flat the following doc: %s", doc), e);
        }

        return new JSONObject(newDoc);
    }


    private void flattenRecursive(String rootName,
                                  Object doc,
                                  Map<String, Object> newDoc,
                                  String separatorChar,
                                  int maxDocLevelsSupported,
                                  List<Class<?>> unSupportedTypes) {
        if (doc instanceof JSONObject) {
            JSONObject document = (JSONObject) doc;
            Map<String, Object> objectMap = document.toMap();

            if (objectMap == null) return;

            objectMap.forEach((field, fieldValue) -> {
                String key = validateFieldName(field, separatorChar);

                if (rootName != null) {
                    key = rootName + separatorChar + key;
                }

                if (isFieldDocDepthValid(key, maxDocLevelsSupported, separatorChar)) {

                    if (fieldValue != null) {
                        if (isTypeValid(fieldValue.getClass(), unSupportedTypes)) {
                            if (fieldValue instanceof HashMap) {
                                flattenRecursive(key, new JSONObject((HashMap) fieldValue), newDoc, separatorChar, maxDocLevelsSupported, unSupportedTypes);
                            } else {
                                newDoc.put(key, fieldValue);
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public JSONObject unFlatten(String doc, String separatorChar) throws FlattenXsonException {
        JSONObject result;

        try {
            JSONObject jsonObject = new JSONObject(doc);
            HashMap newDoc = unFlattenHelper(jsonObject.keySet(), separatorChar, jsonObject);
            result = new JSONObject(newDoc);
        }catch (Exception e){
            throw new UnflattenException(
                    String.format("An error occurred while trying to unflat the following doc: %s", doc), e);
        }

        return result;
    }
}
