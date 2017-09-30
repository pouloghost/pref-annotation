package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.BooleanPreference;

public class BooleanVisiter extends GenVisitor<BooleanPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder builder, String key, BooleanPreference annotation) {
        builder.append("  public static boolean get").append(key).append("(){\n");
        builder.append("    return sPreferences.getBoolean(\"").append(key).append("\", ").append(annotation.def()).append(");");
        builder.append("\n  }\n");
        builder.append("  public static void set").append(key).append("(boolean value){\n").
                append("    sPreferences.edit().putBoolean(\"").append(key).append("\", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(BooleanPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(BooleanPreference annotation) {
        return annotation.prefName();
    }
}
