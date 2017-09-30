package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.LongPreference;

public class LongVisitor extends GenVisitor<LongPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder builder, String key, LongPreference annotation) {
        builder.append("  public static long get").append(key).append("(){\n");
        builder.append("    return sPreferences.getLong(\"").append(key).append("\", ").append(annotation.def()).append("L);");
        builder.append("\n  }\n");
        builder.append("  public static void set").append(key).append("(long value){\n").
                append("    sPreferences.edit().putLong(\"").append(key).append("\", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(LongPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(LongPreference annotation) {
        return annotation.prefName();
    }
}
