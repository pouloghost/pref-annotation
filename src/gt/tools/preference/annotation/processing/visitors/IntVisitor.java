package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.IntPreference;

import javax.lang.model.element.Element;

public class IntVisitor extends GenVisitor<IntPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports,
                                      Element field, String key, String prefixedKey, IntPreference annotation) {
        getterSetter.append("  public static int get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getInt(").append(prefixedKey).append(", ")
                .append(annotation.def()).append(");");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(int value){\n")
                .append("    sPreferences.edit().putInt(").append(prefixedKey)
                .append(", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(IntPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefFile(IntPreference annotation) {
        return annotation.prefFile();
    }

    @Override
    public String getPrefixKey(IntPreference annotation) {
        return annotation.prefixKey();
    }

    @Override
    public String[] acceptableFieldTypes() {
        return new String[]{TYPE_INT};
    }
}