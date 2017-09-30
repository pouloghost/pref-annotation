package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.StringPreference;

public class StringVisitor extends GenVisitor<StringPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder builder, String key, StringPreference annotation) {
        builder.append("  public static String get").append(key).append("(){\n");
        builder.append("    return sPreferences.getString(\"").append(key).append("\", \"").append(annotation.def()).append("\");");
        builder.append("\n  }\n");
        builder.append("  public static void set").append(key).append("(String value){\n").
                append("    sPreferences.edit().putString(\"").append(key).append("\", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(StringPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(StringPreference annotation) {
        return annotation.prefName();
    }
}
