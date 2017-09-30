package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.FloatPreference;

public class FloatVisitor extends GenVisitor<FloatPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder builder, String key, FloatPreference annotation) {
        builder.append("  public static float get").append(key).append("(){\n");
        builder.append("    return sPreferences.getFloat(\"").append(key).append("\", ").append(annotation.def()).append("F);");
        builder.append("\n  }\n");
        builder.append("  public static void set").append(key).append("(float value){\n").
                append("    sPreferences.edit().putFloat(\"").append(key).append("\", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(FloatPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(FloatPreference annotation) {
        return annotation.prefName();
    }
}
