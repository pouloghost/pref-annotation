package gt.tools.preference.annotation;

import java.lang.reflect.Type;

public class PreferenceContext {
    private static PreferenceAdapter sImpl;

    public static void init(PreferenceAdapter context) {
        sImpl = context;
    }

    public static <T> T get(String key) {
        return sImpl.getPreferenceByName(key);
    }

    public static String getPreferenceKeyPrefix(String key) {
        return sImpl.getPreferenceKeyPrefix(key);
    }

    public static <D> D deserialize(String json, Type type) {
        return (D) sImpl.deserialize(json, type);
    }

    public static String serialize(Object obj) {
        return sImpl.serialize(obj);
    }
}

