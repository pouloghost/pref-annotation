package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.LongPreference;

import javax.lang.model.element.Element;

public class LongVisitor extends GenVisitor<LongPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports,
                                      Element field, String key, String prefixedKey, LongPreference annotation) {
        getterSetter.append("  public static long get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getLong(").append(prefixedKey).append(", ")
                .append(annotation.def()).append("L);");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(long value){\n")
                .append("    sPreferences.edit().putLong(").append(prefixedKey)
                .append(", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(LongPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefFile(LongPreference annotation) {
        return annotation.prefFile();
    }

    @Override
    public String getPrefixKey(LongPreference annotation) {
        return annotation.prefixKey();
    }

    @Override
    public String[] acceptableFieldTypes() {
        return new String[]{TYPE_LONG};
    }

    @Override
    public boolean removable(LongPreference annotation) {
        return annotation.removable();
    }
}

