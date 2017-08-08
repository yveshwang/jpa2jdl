package io.github.jhipster.jpa2jdl;

import io.github.jhipster.jpa2jdl.field.FieldDefinition;
import io.github.jhipster.jpa2jdl.field.FieldProcessor;
import io.github.jhipster.jpa2jdl.relationship.RelationsCache;
import io.github.jhipster.jpa2jdl.relationship.RelationshipDefinition;
import io.github.jhipster.jpa2jdl.relationship.RelationshipProcessor;
import io.github.jhipster.jpa2jdl.relationship.RelationshipType;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

public class ReverseJPA2JDL {
    private static final Logger LOG = LoggerFactory.getLogger(ReverseJPA2JDL.class);
    public static final String EMPTY_PREFIX = "";
    private RelationsCache relations = new RelationsCache();
    public String debuginfo() {
        final StringBuilder builder = new StringBuilder();
        final List<RelationsCache.Relation> list = relations.getDebug();
        for(final RelationsCache.Relation relation: list) {
            builder.append(relation.toString() + "\n");
        }
        return builder.toString();
    }
    public String generate(Set<Class<?>> entitySubClasses, Set<Class<?>> enums ) {
        final StringBuilder jdl = new StringBuilder();
        for(Class<?> e : enums) {
            generateEnum2Jdl(jdl, e);
        }
        for(Class<?> e : entitySubClasses) {
            generateClass2Jdl(e, jdl);
        }
        generateRelations2Jdl(jdl, RelationsCache.RelationType.OneToOne.name(), new ArrayList<>(relations.getOneToOne().values()));
        generateRelations2Jdl(jdl, RelationsCache.RelationType.OneToMany.name(), new ArrayList<>(relations.getOneToMany().values()));
        generateRelations2Jdl(jdl, RelationsCache.RelationType.ManyToOne.name(), new ArrayList<>(relations.getManyToOne().values()));
        generateRelations2Jdl(jdl, RelationsCache.RelationType.ManyToMany.name(), new ArrayList<>(relations.getManyToMany().values()));
        for(Class<?> e : entitySubClasses) {
            generatePagination(jdl, e);
        }
        for(Class<?> e : entitySubClasses) {
            generateDto(jdl, e);
        }
        for(Class<?> e : entitySubClasses) {
            generateServices(jdl, e);
        }
        return jdl.toString();
    }
    private void genreateOption(final String prefix, final String suffix, final StringBuilder out, final Class<?> e) {
        final String entityClassName = e.getSimpleName();
        out.append(prefix + " " + entityClassName + " " + suffix + "\n");
    }
    public void generatePagination(final StringBuilder out, final Class<?> e) {
        genreateOption("paginate", "with pager", out, e);
    }
    public void generateServices(final StringBuilder out, final Class<?> e) {
        genreateOption("service", "with serviceClass", out, e);
    }
    public void generateDto(final StringBuilder out, final Class<?> e) {
        genreateOption("dto", "with mapstruct", out, e);
    }
    public void generateEnum2Jdl(final StringBuilder out, final Class<?> e) {
        final String entityClassName = e.getSimpleName();
        boolean firstField = true;
        out.append("enum " + entityClassName + " {\n");
        final Field[] declaredFields = e.getDeclaredFields();
        for(final Field f : declaredFields) {
            final String fieldName = f.getName();
            if (f.isSynthetic() || !Modifier.isStatic(f.getModifiers()) || !Modifier.isFinal(f.getModifiers())) {
                continue;
            }
            if (firstField) {
                firstField = false;
            } else {
                out.append(",\n");
            }
            out.append("  " + fieldName);
        }
        out.append("\n");
        out.append("}\n\n");
    }
    public void generateRelations2Jdl(final StringBuilder relationShips, final String relationType, final List<RelationsCache.Relation> relations) {
        relationShips.append("relationship " + relationType + " {\n");
        for(int i = 0; i < relations.size(); i++) {
            final RelationsCache.Relation rel = relations.get(i);
            relationShips.append("\t");
            relationShips.append(rel.toStringUntabbed());
            if( i < relations.size() -1) {
                relationShips.append(",");
            }
            relationShips.append("\n");
        }
        relationShips.append("}\n\n");
    }

    public void generateClass2Jdl(final Class<?> e, final StringBuilder out) {
        boolean isFirstField = true;
        out.append("entity " + e.getSimpleName() + " {\n"); // inheritance NOT SUPPORTED YET in JDL ???

        final Field[] declaredFields = e.getDeclaredFields();
        for(final Field f : declaredFields) {
            boolean wasGenerated = generateField(f, out, isFirstField, EMPTY_PREFIX);
            if (isFirstField && wasGenerated) isFirstField = false;
        }
        out.append("\n");
        out.append("}\n\n");
    }

    private boolean generateEmbedded(final Class e, final StringBuilder out, final boolean firstField, final String prefix) {
        boolean generatedFields = false;
        boolean isFirstField = firstField;

        if (isNull(e.getDeclaredAnnotation(Embeddable.class))) {
            return false;
        }
        for (final Field f : e.getDeclaredFields()) {
            boolean wasGenerated = generateField(f, out, isFirstField, prefix);
            if (firstField && wasGenerated) {
                generatedFields = true;
            }
            if (wasGenerated) {
                isFirstField = false;
            }
        }
        return generatedFields;
    }

    private boolean generateField(final Field f, final StringBuilder out, final boolean firstField, final String prefix) {
        if (f.isSynthetic() || Modifier.isStatic(f.getModifiers())) {
            return false;
        }
        if (f.getDeclaredAnnotation(Transient.class) != null) {
            return false;
        }
        if (f.getDeclaredAnnotation(Id.class) != null) {
            return false;
        }

        if (f.getDeclaredAnnotation(Embedded.class) != null) {
            return generateEmbedded(f.getType(), out, firstField, FieldDefinition.toDBName(f, prefix));
        }

        final Type declaredAnnotation = f.getDeclaredAnnotation(Type.class);
        if (declaredAnnotation != null) {
            return generateType(f, declaredAnnotation, out, firstField, prefix);
        }


        final Optional<RelationshipDefinition> relationship = RelationshipProcessor.process(f);
        relationship.ifPresent(r -> {
            final String targetEntityClassName = r.getTargetEntityClass()
                .map(Class::getSimpleName)
                .orElse("");

            if (r.getRelationType().equals(RelationshipType.ManyToMany)) {
                LOG.warn("ManyToMany .. mappedBy ??");
            }
            relations.addRelation(f.getType().getSimpleName(), f.getName(), targetEntityClassName, r.getMappedBy(), r.getRelationTypeName());
        });

        if (!relationship.isPresent()) {
            final FieldDefinition fd = FieldProcessor.process(f);
            fd.generateSimpleField(firstField, prefix, out);
            return true;
        }
        return false;
    }

    private boolean generateType(final Field f, final Type declaredAnnotation, final StringBuilder out, final boolean firstField, final String prefix) {
        switch (declaredAnnotation.type()) {
            case "org.jadira.usertype.moneyandcurrency.joda.PersistentCurrencyUnit" :
                FieldProcessor
                    .processCurrency(f)
                    .generateCurrencyField(out, prefix, firstField);
                return true;

            case "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmountAndCurrency" :
                FieldProcessor
                    .processMoney(f)
                    .generateMoneyField(out, prefix, firstField);
                return true;

            default:
                LOG.warn("unrecognized type {}", declaredAnnotation.type());
                return false;
        }
    }
}
