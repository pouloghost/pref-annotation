package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.processing.PreferenceProcessor;
import gt.tools.preference.annotation.processing.PreferenceTemplate;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;

public abstract class GenVisitor<A> {
    protected final String buildPrefKey(Element field, String key) {
        if (field == null) {
            return key;
        }
        // 默认用field name
        if ("".equals(key)) {
            key = field.getSimpleName().toString();
            if (key.startsWith("m") && key.charAt(1) >= 'A' && key.charAt(1) <= 'Z') {
                key = key.substring(1);
            }
            if (key.charAt(0) >= 'a' && key.charAt(0) <= 'z') {
                key = key.substring(0, 1).toUpperCase() + key.substring(1);
            }
        }
        String[] parts = key.split("_");
        if (parts.length == 1) {
            return key;
        }
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            part = part.toLowerCase();
            builder.append(part.substring(0, 1).toUpperCase()).append(part.substring(0));
        }
        return builder.toString();
    }

    protected final void checkKey(StringBuilder builder, String key) {
        if (builder.indexOf("get" + key + "()") != -1) {
            PreferenceProcessor.exception("key clashed " + key);
        }
    }

    protected abstract void appendGetterSetter(StringBuilder builder, String key, A annotation);

    protected void appendSaver(PreferenceTemplate pref, TypeElement root, Element field, String key) {
        if (root == null) {
            return;
        }
        StringBuilder saver = pref.mSavers.get(root.getSimpleName().toString());
        if (saver == null) {
            saver = new StringBuilder();
            pref.mSavers.put(root.getSimpleName().toString(), saver);
            saver.append("  public static void save(").append(root.getSimpleName().toString()).append(" object){\n}");
            pref.mImports.append("import ").append(root.getQualifiedName()).append(";\n");
        }
        saver.delete(saver.lastIndexOf("}"), saver.length());
        saver.append("    set").append(key).append("(object.").append(field.getSimpleName().toString()).append(");\n}\n");
    }

    public void visit(PreferenceTemplate template, TypeElement root, Element field, Annotation annotation) {
        String key = buildPrefKey(field, getAnnotationKey((A) annotation));
        checkKey(template.mGetterSetters, key);
        appendGetterSetter(template.mGetterSetters, key, (A) annotation);
        appendSaver(template, root, field, key);
    }

    protected abstract String getAnnotationKey(A annotation);

    public abstract String getPrefName(A annotation);
}
