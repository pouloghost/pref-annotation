package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.IntPreference;

public class IntVisitor extends GenVisitor<IntPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder builder, String key, IntPreference annotation) {
        builder.append("  public static int get").append(key).append("(){\n");
        builder.append("    return sPreferences.getInt(\"").append(key).append("\", ").append(annotation.def()).append(");");
        builder.append("\n  }\n");
        builder.append("  public static void set").append(key).append("(int value){\n").
                append("    sPreferences.edit().putInt(\"").append(key).append("\", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(IntPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(IntPreference annotation) {
        return annotation.prefName();
    }
}
