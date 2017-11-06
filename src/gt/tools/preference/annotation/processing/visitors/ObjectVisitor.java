package gt.tools.preference.annotation.processing.visitors;


import gt.tools.preference.annotation.ObjectPreference;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

public class ObjectVisitor extends GenVisitor<ObjectPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports,
                                      Element field, String key, String prefixedKey, ObjectPreference annotation) {
        TypeElement fieldType = (TypeElement) ((DeclaredType) field.asType()).asElement();
        if (imports.indexOf("import java.lang.reflect.Type;") == -1) {
            imports.append("import java.lang.reflect.Type;\n");
        }
        if (imports.indexOf(fieldType.getQualifiedName().toString()) == -1) {
            imports.append("import ").append(fieldType.getQualifiedName().toString()).append(";\n");
        }
        getterSetter.append("  public static ").append(fieldType.getSimpleName()).append(" get")
                .append(key).append("(Type clazz){\n")
                .append("    String value = sPreferences.getString(").append(prefixedKey)
                .append(", \"").append(annotation.def().replace("\"", "\\\"")).append("\");\n")
                .append("    if(value == null){\n")
                .append("      return null;\n")
                .append("    }\n")
                .append("    return (").append(fieldType.getSimpleName())
                .append(") PreferenceContext.deserialize(value, clazz);\n")
                .append("  }\n");

        getterSetter.append("  public static void set").append(key).append("(")
                .append(fieldType.getSimpleName()).append(" value){\n")
                .append("    sPreferences.edit().putString(").append(prefixedKey)
                .append(", PreferenceContext.serialize(value)).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(ObjectPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefFile(ObjectPreference annotation) {
        return annotation.prefFile();
    }

    @Override
    public String getPrefixKey(ObjectPreference annotation) {
        return annotation.prefixKey();
    }

    @Override
    public String[] acceptableFieldTypes() {
        return new String[]{TYPE_OBJECT};
    }

    @Override
    public boolean removable(ObjectPreference annotation) {
        return annotation.removable();
    }
}
