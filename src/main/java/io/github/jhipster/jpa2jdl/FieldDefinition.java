package io.github.jhipster.jpa2jdl;

import org.springframework.util.StringUtils;

import javax.persistence.Lob;
import java.lang.reflect.Field;

public class FieldDefinition {
    private final Field field;
    private final boolean required;
    private final int min;
    private final int max;
    private final boolean isBlob;
    private final String pattern;

    public FieldDefinition(final Field field, final boolean required, final int min, final int max, final boolean isBlob, final String pattern) {
        this.field = field;
        this.required = required;
        this.min = min;
        this.max = max;
        this.isBlob = isBlob;
        this.pattern = pattern;
    }

    public Field getField() {
        return field;
    }

    public boolean isRequired() {
        return required;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean isBlob() {
        return isBlob;
    }

    public String getPattern() {
        return pattern;
    }

    public static String toDBName(final Field field, final String prefix) {
        return StringUtils.isEmpty(prefix) ? field.getName() : prefix.concat(StringUtils.capitalize(field.getName()));
    }

    public void generateSimpleField(final boolean firstField, final String prefix, StringBuilder out) {
        // simple field
        if (!firstField) {
            out.append(",\n");
        }
        out.append("  " + toDBName(this.getField(), prefix) + " " + getTypeName(this.getField()) + (this.isRequired() ? " required" : "")
            + ( (this.getMin() > -1) ? " " + getMinLabel(this.getField()) + "(" + this.getMin() + ")" : "")
            + ( (this.getMax() > -1) ? " " + getMaxLable(this.getField()) + "(" + this.getMax() + ")" : "")
            + ( (this.getPattern() != null) ? " pattern(\"" + this.getPattern() + "\")": ""));
    }

    private String getMinLabel(final Field f) {
        if(f.getDeclaredAnnotation(Lob.class) != null) {
            return "minbytes";
        } else {
            if( f.getType().equals(String.class)) {
                return "minlength";
            } else {
                return "min";
            }
        }
    }
    private String getMaxLable(final Field f) {
        if(f.getDeclaredAnnotation(Lob.class) != null) {
            return "maxbytes";
        } else {
            if( f.getType().equals(String.class)) {
                return "maxlength";
            } else {
                return "max";
            }
        }
    }
    private String getTypeName(final Field f) {
        if( f.getDeclaredAnnotation(Lob.class) != null) {
            if( f.getType().equals(String.class)) {
                return "TextBlob";
            } else {
                return "AnyBlob";
            }
        } else {
            return f.getType().getSimpleName() ;
        }
    }
}