package gt.tools.preference.annotation;

public interface IProvider<T> {
    T get(String key);
}
