package io.github.jhipster.jpa2jdl;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;

public class FieldProcessor {

    public static FieldDefinition process(final Field f) {
        boolean required = false;
        int min = -1;
        int max = -1;
        boolean isBlob = false;
        String pattern = null;
        if( f.getDeclaredAnnotation(NotNull.class) != null) {
            required = true;
        }
        final Size size = f.getDeclaredAnnotation(Size.class);
        if (size != null) {
            if( size.max() < Integer.MAX_VALUE) {
                max = size.max();
            }
            if (size.min() > 0) {
                min = size.min();
            }
        }

        final Column col = f.getDeclaredAnnotation(Column.class);
        if( col != null) {
            if(!col.nullable()) {
                required = true;
            }
            if( col.length() > 0 && f.getType().equals(String.class) && col.length() != 255) {
                //XXX: yves 23.06.2017, 255 is the default value for length
                max = col.length();
            }
        }
        if( f.getDeclaredAnnotation(Lob.class) != null) {
            isBlob = true;
        }
        final Pattern pat = f.getDeclaredAnnotation(Pattern.class);

        if( pat != null && pat.regexp() != null) {
            pattern = pat.regexp();
        }

        return new FieldDefinition(f, required, min, max, isBlob, pattern);
    }
}
