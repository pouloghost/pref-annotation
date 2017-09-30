package gt.tools.preference.annotation;

import java.util.HashMap;
import java.util.Map;

public class ProviderContext<T> {
    private Map<String, T> mProvider = new HashMap<>();

    private static ProviderContext sInstance;

    public static <T> void init(Map<String, T> provider) {
        sInstance = new ProviderContext();
        sInstance.mProvider = provider;
    }

    public static final ProviderContext getInstance() {
        return sInstance;
    }

    public T get(String key) {
        return mProvider.get(key);
    }
}
