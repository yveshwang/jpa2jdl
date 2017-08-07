package io.github.jhipster.jpa2jdl;

import com.google.common.base.Objects;

import java.util.Optional;

public final class RelationshipDefinition {
    RelationshipType relationType = null;
    Optional<Class<?>> targetEntityClass = Optional.empty();
    String mappedBy = "";

    public RelationshipDefinition(final RelationshipType type, final Class<?> targetEntityClass, final String mappedBy) {
        this.relationType = type;
        this.targetEntityClass = Optional.of(targetEntityClass);
        this.mappedBy = mappedBy;
    }

    public String getRelationTypeName() {
        return relationType.getName();
    }

    public RelationshipType getRelationType() {
        return relationType;
    }

    public Optional<Class<?>> getTargetEntityClass() {
        return targetEntityClass;
    }


    public String getMappedBy() {
        return mappedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationshipDefinition that = (RelationshipDefinition) o;
        return relationType == that.relationType &&
            Objects.equal(targetEntityClass, that.targetEntityClass) &&
            Objects.equal(mappedBy, that.mappedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(relationType, targetEntityClass, mappedBy);
    }
}