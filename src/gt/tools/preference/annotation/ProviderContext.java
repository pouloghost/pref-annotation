package gt.tools.preference.annotation;

public class ProviderContext<T> {
    private IProvider<T> mProvider;

    private static ProviderContext sInstance;

    public static <T> void init(IProvider<T> provider) {
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
