package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.StringPreference;

import javax.lang.model.element.Element;

public class StringVisitor extends GenVisitor<StringPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports, Element field, String key, StringPreference annotation) {
        getterSetter.append("  public static String get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getString(\"").append(key).append("\", \"").append(annotation.def()).append("\");");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(String value){\n").
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
