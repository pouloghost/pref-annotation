package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.StringPreference;

import javax.lang.model.element.Element;

public class StringVisitor extends GenVisitor<StringPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports,
                                      Element field, String key, String prefixedKey, StringPreference annotation) {
        getterSetter.append("  public static String get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getString(").append(prefixedKey).append(", \"")
                .append(annotation.def()).append("\");");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(String value){\n")
                .append("    sPreferences.edit().putString(").append(prefixedKey)
                .append(", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(StringPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefFile(StringPreference annotation) {
        return annotation.prefFile();
    }

    @Override
    public String getPrefixKey(StringPreference annotation) {
        return annotation.prefixKey();
    }

    @Override
    public String[] acceptableFieldTypes() {
        return new String[]{TYPE_STRING};
    }

    @Override
    public boolean removable(StringPreference annotation) {
        return annotation.removable();
    }
}
