package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.FloatPreference;

import javax.lang.model.element.Element;

public class FloatVisitor extends GenVisitor<FloatPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports,
                                      Element field, String key, String prefixedKey, FloatPreference annotation) {
        getterSetter.append("  public static float get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getFloat(").append(prefixedKey).append(", ")
                .append(annotation.def()).append("F);");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(float value){\n")
                .append("    sPreferences.edit().putFloat(").append(prefixedKey)
                .append(", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(FloatPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefFile(FloatPreference annotation) {
        return annotation.prefFile();
    }

    @Override
    public String getPrefixKey(FloatPreference annotation) {
        return annotation.prefixKey();
    }

    @Override
    public String[] acceptableFieldTypes() {
        return new String[] {TYPE_FLOAT};
    }
}

