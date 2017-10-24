package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.processing.PreferenceProcessor;
import gt.tools.preference.annotation.processing.PreferenceTemplate;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public abstract class GenVisitor<A> {
    static final String TYPE_BOOLEAN = "boolean";
    static final String TYPE_FLOAT = "float";
    static final String TYPE_INT = "int";
    static final String TYPE_LONG = "long";
    static final String TYPE_OBJECT = "*";
    static final String TYPE_STRING = "java.lang.String";
    static final List<String> PRIMITIVES =
            Arrays.asList(TYPE_BOOLEAN, TYPE_FLOAT, TYPE_INT, TYPE_LONG, TYPE_STRING);

    protected final String buildPrefKey(A annotation, String key) {
        String prefix = getPrefixKey(annotation);
        if ("".equals(prefix)) {
            return key;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("PreferenceContext.getPreferenceKeyPrefix(\"")
                .append(prefix)
                .append("\") + \"")
                .append(key)
                .append("\"");
        return builder.toString();
    }

    private String formatKey(Element field, String key) {
        if (field == null) {
            return key;
        }
        // 默认用field name
        if ("".equals(key)) {
            key = field.getSimpleName().toString();
        }
        String[] parts = key.split("_");
        if (parts.length == 1) {
            if (key.startsWith("m") && key.charAt(1) >= 'A' && key.charAt(1) <= 'Z') {
                key = key.substring(1);
            }
            if (key.charAt(0) >= 'a' && key.charAt(0) <= 'z') {
                key = key.substring(0, 1).toUpperCase() + key.substring(1);
            }
            return key;
        }
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.length() != 0) {
                part = part.toLowerCase();
                builder.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
            }
        }
        return builder.toString();
    }

    protected final void checkKey(StringBuilder builder, String key) {
        if (builder.indexOf("get" + key + "()") != -1) {
            PreferenceProcessor.exception("key clashed " + key, null);
        }
    }

    protected abstract void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports,
                                               Element field, String key, String prefixedKey, A annotation);

    protected void appendSaver(PreferenceTemplate pref, TypeElement root, Element field, String key) {
        if (root == null) {
            return;
        }
        StringBuilder saver = pref.mSavers.get(root.getSimpleName().toString());
        if (saver == null) {
            saver = new StringBuilder();
            pref.mSavers.put(root.getSimpleName().toString(), saver);
            saver.append("  public static void save(").append(root.getSimpleName().toString())
                    .append(" object){\n}");
            pref.mImports.append("import ").append(root.getQualifiedName()).append(";\n");
        }
        saver.delete(saver.lastIndexOf("}"), saver.length());
        saver.append("    set").append(key).append("(object.").append(field.getSimpleName().toString())
                .append(");\n  }\n");
    }

    public void visit(PreferenceTemplate template, TypeElement root, Element field,
                      Annotation annotation, boolean needSaver) {
        checkType(field);
        String key = getAnnotationKey((A) annotation);
        String pureKey = formatKey(field, key);
        String prefixedKey = buildPrefKey((A) annotation, "".equals(key) ? pureKey : key);
        checkKey(template.mGetterSetters, pureKey);
        appendGetterSetter(template.mGetterSetters, template.mImports, field, pureKey, prefixedKey,
                (A) annotation);
        if (needSaver) {
            appendSaver(template, root, field, pureKey);
        }
    }

    private void checkType(Element field) {
        String type = field.asType().toString();
        List<String> types = Arrays.asList(acceptableFieldTypes());
        if (types.contains(TYPE_OBJECT)) {
            if (PRIMITIVES.contains(type)) {
                PreferenceProcessor.exception("type must be correct", field);
            }
        } else if (!types.contains(type)) {
            PreferenceProcessor.exception("type must be correct", field);
        }
    }

    protected abstract String getAnnotationKey(A annotation);

    public abstract String getPrefFile(A annotation);

    public abstract String getPrefixKey(A annotation);

    public abstract String[] acceptableFieldTypes();
}
