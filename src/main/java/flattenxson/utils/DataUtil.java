package flattenxson.utils;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DataUtil {
    public static Map jsonStringToMap(String jsonDoc) throws IOException {
        return new ObjectMapper().readValue(jsonDoc, HashMap.class);
    }

    public static boolean isListExistNumericValue(List<String> list) {
        for (String item : list) {
            if (StringUtils.isNumeric(item)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNextKeyIsArrayIndex(List<String> list) {
        boolean result = false;

        if (list.size() >= 2) {
            if (StringUtils.isNumeric(list.get(0))) {
                result = true;
            }
        }

        return result;
    }
}
