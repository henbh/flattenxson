package flattenxson.utils;

import flattenxson.exceptions.FlattenXsonException;
import flattenxson.iml.JsonImpl;
import flattenxson.iml.BaseFlatten;
import org.json.JSONObject;

import java.util.List;

public class JsonFlattenUtil {
    public static JSONObject flattenDoc(Object doc, String separatorChar, int maxDocLevelsSupported, List<Class<?>> unSupportedTypes) throws FlattenXsonException {
        BaseFlatten baseFlatten = new JsonImpl();
        return (JSONObject) baseFlatten.flatten(doc, separatorChar, maxDocLevelsSupported, unSupportedTypes);
    }

    public static JSONObject flattenDoc(Object doc, String separatorChar, int maxDocLevelsSupported) throws FlattenXsonException {
        BaseFlatten baseFlatten = new JsonImpl();
        return (JSONObject) baseFlatten.flatten(doc, separatorChar, maxDocLevelsSupported, null);
    }

    public static JSONObject flattenDoc(Object doc, String separatorChar) throws FlattenXsonException {
        BaseFlatten baseFlatten = new JsonImpl();
        return (JSONObject) baseFlatten.flatten(doc, separatorChar, 0, null);
    }

    public static JSONObject flattenDoc(Object doc) throws FlattenXsonException {
        BaseFlatten baseFlatten = new JsonImpl();
        return (JSONObject) baseFlatten.flatten(doc, "__", 0, null);
    }

    public static JSONObject flattenDoc(Object doc, int maxDocLevelsSupported) throws FlattenXsonException {
        BaseFlatten baseFlatten = new JsonImpl();
        return (JSONObject) baseFlatten.flatten(doc, "__", maxDocLevelsSupported, null);
    }

    public static JSONObject flattenDoc(Object doc, int maxDocLevelsSupported, List<Class<?>> unSupportedTypes) throws FlattenXsonException {
        BaseFlatten baseFlatten = new JsonImpl();
        return (JSONObject) baseFlatten.flatten(doc, "__", maxDocLevelsSupported, unSupportedTypes);
    }

    public static JSONObject unFlatten(String doc, String separatorChar) throws FlattenXsonException {
        BaseFlatten baseFlatten = new JsonImpl();
        return (JSONObject) baseFlatten.unFlatten(doc,separatorChar);
    }

    public static JSONObject unFlatten(String doc) throws FlattenXsonException {
        BaseFlatten baseFlatten = new JsonImpl();
        return (JSONObject) baseFlatten.unFlatten(doc,"__");
    }
}
