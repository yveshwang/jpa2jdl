package io.github.jhipster.jpa2jdl.relationship;

import org.springframework.util.StringUtils;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

public class RelationshipProcessor {

    public static Optional<RelationshipDefinition> process(final Field f) {
        final OneToMany oneToManyAnnotation = f.getDeclaredAnnotation(OneToMany.class);
        if (oneToManyAnnotation != null) {
            return Optional.of(
                new RelationshipDefinition(
                    RelationshipType.OneToMany,
                    resolveTargetEntityClass(f, oneToManyAnnotation.targetEntity(), RelationshipType.OneToMany),
                    resolveMappedBy(oneToManyAnnotation.mappedBy())
                )
            );
        }
        final OneToOne oneToOneAnnotation = f.getDeclaredAnnotation(OneToOne.class);
        if (oneToOneAnnotation != null) {
            return Optional.of(
                new RelationshipDefinition(
                    RelationshipType.OneToOne,
                    resolveTargetEntityClass(f, oneToOneAnnotation.targetEntity(), RelationshipType.OneToOne),
                    resolveMappedBy(oneToOneAnnotation.mappedBy())
                )
            );
        }
        final ManyToMany manyToManyAnnotation = f.getDeclaredAnnotation(ManyToMany.class);
        if (manyToManyAnnotation != null) {
            return Optional.of(
                new RelationshipDefinition(
                    RelationshipType.ManyToMany,
                    resolveTargetEntityClass(f, manyToManyAnnotation.targetEntity(), RelationshipType.ManyToMany),
                    resolveMappedBy(manyToManyAnnotation.mappedBy())
                )
            );
        }
        final ManyToOne manyToOneAnnotation = f.getDeclaredAnnotation(ManyToOne.class);
        if (manyToOneAnnotation != null) {
            return Optional.of(
                new RelationshipDefinition(
                    RelationshipType.ManyToOne,
                    resolveTargetEntityClass(f, manyToOneAnnotation.targetEntity(), RelationshipType.ManyToOne),
                    resolveMappedBy("")
                )
            );
        }
        return Optional.empty();
    }

    private static Class<?> resolveTargetEntityClass(final Field f, final Class targetEntityClass, final RelationshipType type) {
        Class<?> result = targetEntityClass;
        if (targetEntityClass == void.class || targetEntityClass == null) {
            result = typeToClass(f.getType());
        }

        if (type.isToMany() && targetEntityClass != null && Collection.class.isAssignableFrom(targetEntityClass)) {
            final Class<?> compType = targetEntityClass.getComponentType();
            if (compType != null) {
                result = compType;
            } else {
                final Type fieldGenericType = f.getGenericType();
                if (fieldGenericType instanceof ParameterizedType) {
                    final ParameterizedType pt = (ParameterizedType) fieldGenericType;
                    result = typeToClass(pt.getActualTypeArguments()[0]);
                }
            }
        }
        return result;
    }

    private static String resolveMappedBy(final String val) {
        return !StringUtils.isEmpty(val) ? val : null;
    }

    private static Class<?> typeToClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return typeToClass(((ParameterizedType) type).getRawType());
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> componentClass = typeToClass(componentType);
            if (componentClass != null) {
                return Array.newInstance(componentClass, 0).getClass();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
