package gt.tools.preference.annotation;


import java.lang.reflect.Type;

public interface PreferenceAdapter {
    <T> T getPreferenceByName(String key);

    String getPreferenceKeyPrefix(String key);

    <D> D deserialize(String json, Type typeOfD);

    String serialize(Object obj);
}