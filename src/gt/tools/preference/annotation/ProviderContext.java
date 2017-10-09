package gt.tools.preference.annotation;

import java.util.HashMap;
import java.util.Map;

public class ProviderContext<T> {
    private Map<String, T> mProvider = new HashMap<>();
    private Jsons mJsons;

    private static ProviderContext sInstance;

    public static <T> void init(Map<String, T> provider, Jsons jsons) {
        sInstance = new ProviderContext();
        sInstance.mProvider = provider;
        sInstance.mJsons = jsons == null ? new Jsons() : jsons;
    }

    public static final ProviderContext getInstance() {
        return sInstance;
    }

    public T get(String key) {
        return mProvider.get(key);
    }

    public Jsons jsons() {
        return mJsons;
    }
}
