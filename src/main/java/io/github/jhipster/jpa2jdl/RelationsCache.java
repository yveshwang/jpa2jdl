package io.github.jhipster.jpa2jdl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by yveshwang on 26/06/2017.
 */
public class RelationsCache {
    private static final Logger LOG = LoggerFactory.getLogger(RelationsCache.class);
    private final HashMap<Integer, RelationsCache.Relation> oneToOne = new HashMap<>();
    private final HashMap<Integer, RelationsCache.Relation> oneToMany = new HashMap<>();
    private final HashMap<Integer, RelationsCache.Relation> manyToOne = new HashMap<>();
    private final HashMap<Integer, RelationsCache.Relation> manyToMany = new HashMap<>();

    public RelationsCache() {
        // empty
    }

    public void addRelation(String leftClass, String leftField, String rightClass, String rightField, String type) {
        final RelationsCache.Relation r = new Relation(new EntityDesc(leftClass, leftField), new EntityDesc(rightClass, rightField), RelationsCache.RelationType.valueOf(type));
        switch(r.type) {
            case OneToOne:
                // 1-1
                // scenario one_to_one 1: if a bidirectional relations is already added, then unidirection with the same field is ignored.
                // scenario one_to_one 2: if uni is already added, and adding another bidirection of inverse ownership, will upgrade the initial unidireciton into bidirectional with the new field.
                if( !oneToOne.containsKey(Integer.valueOf(r.hashCode()))) {
                    if( r.bidirectional) {
                        //scenario 2 - inverse unidirectional already added, so upgrade uni to bi with the updated field value
                        final Relation inverse = oneToOne.values().stream()
                                .filter(relation -> !relation.bidirectional)
                                .filter(relation -> (relation.left.hashCode() == r.right.hashCode() && relation.right.className.hashCode() == r.left.className.hashCode()))
                                .findAny().orElse(null);
                        if( inverse != null) {
                            oneToOne.remove(Integer.valueOf(inverse.hashCode()));
                            inverse.bidirectional = true;
                            inverse.right.fieldName = r.left.fieldName;
                            oneToOne.put(Integer.valueOf(inverse.hashCode()), inverse);
                        } else {
                            oneToOne.put(Integer.valueOf(r.hashCode()), r);
                        }
                    } else {
                        //scenario 1 -
                        final Relation exists = oneToOne.values().stream()
                                .filter(relation -> relation.bidirectional)
                                .filter(relation -> (relation.left.className.hashCode() == r.right.className.hashCode() && relation.right.hashCode() == r.left.hashCode())
                                        || (relation.left.hashCode() == r.left.hashCode() && relation.right.className.hashCode() == r.right.className.hashCode()))
                                .findAny().orElse(null);
                        if( exists == null) {
                            oneToOne.put(Integer.valueOf(r.hashCode()), r);
                        }
                    }
                }
                break;
            case OneToMany:
                // scenario one_to_many: since jhipster does not support unidirectional version of one_to_many,
                // we do expect this to be bidirectional.
                if(!r.bidirectional) {
                    LOG.warn("Unidirectional OneToMany detected. Relation is ignored. Change this to ManyToOne instead?\n\t" + r.toString() + "\n");
                } else {
                    if( !oneToMany.containsKey(Integer.valueOf(r.hashCode()))) {
                        oneToMany.put(Integer.valueOf(r.hashCode()), r);
                        final Relation exists = manyToOne.values().stream()
                                .filter(relation -> !relation.bidirectional)
                                .filter(relation -> (relation.left.hashCode() == r.right.hashCode() && relation.right.className.hashCode() == r.left.className.hashCode()))
                                .findAny().orElse(null);
                        if( exists != null) {
                            manyToOne.remove(exists.hashCode());
                        }
                    }
                }
                break;
            case ManyToOne:
                // scenario many_to_one: many_to_one is basically the unidirectional version of one_to_many.
                // we expect this to be unidirectional.
                // so if there is a legit unidirectional ManyToOne relationship, first check if there already exists a
                // bidirectional onetomany as the many2one would be redundant.
                if( r.bidirectional) {
                    LOG.warn("Bidirectional ManyToOne detected. Relation is ignored. Change this to OneToMany instead?\n\t" + r.toString() + "\n");
                } else {
                    if( !manyToOne.containsKey(Integer.valueOf(r.hashCode()))
                            && !hasBidirectionalOneToMany(r)) {
                        manyToOne.put(Integer.valueOf(r.hashCode()), r);
                    }
                }
                break;
            case ManyToMany:
                // scenario many_to_many: unidirection because it makes no sense.
                // JPA uses mappedBy attribute to specify the inverse of the relationship.
                // i.e the entity that "owns" the relation will not have the mappedBy attribute set.
                // e.g. @ManyToMany(mappedBy = "tags")
                // where as the owner of the relationship will have the @JoinTable annotation.
                // safe to assume that there will be two ManyToMany relations when parsing JPA entities, 1 that is "unidirectional" and 1 that is "bidirectional".
                // so we look for the bidirectional one and ignore the uni :)
                if( !r.bidirectional) {
                    LOG.warn("Unidirectional ManyToMany detected. Relation is ignored. Computer says no.\n" + r.toString() + "\n");
                } else if( !manyToMany.containsKey(Integer.valueOf(r.hashCode()))) {
                    manyToMany.put(Integer.valueOf(r.hashCode()), r);
                }
                break;
            default:

        }
    }
    private boolean hasBidirectionalOneToMany(final Relation uni) {
        final Iterator<Relation> it = oneToMany.values().iterator();
        while( it.hasNext()) {
            final Relation r = it.next();
            if(r.bidirectional && r.right.className.equals(uni.left.className)
                        && r.left.className.equals(uni.right.className)
                        && uni.left.fieldName.equals(r.right.fieldName)) {
                return true;
            }
        }
        return false;
    }
    public HashMap<Integer, Relation> getOneToOne() {
        return oneToOne;
    }

    public HashMap<Integer, Relation> getOneToMany() {
        return oneToMany;
    }

    public HashMap<Integer, Relation> getManyToOne() {
        return manyToOne;
    }

    public HashMap<Integer, Relation> getManyToMany() {
        return manyToMany;
    }

    public enum RelationType {
        OneToOne, OneToMany, ManyToOne, ManyToMany;
    }

    private static int hash(final Relation r) {
        switch (r.type) {
            case OneToOne:
                return hash_onetoone(r);
            case OneToMany:
                return hash_onetomany(r);
            case ManyToOne:
                return hash_manytoone(r);
            case ManyToMany:
                return hash_manytomany(r);
        }
        return -1;
    }

    private static int hash_manytoone(final Relation r) {
        return  RelationType.ManyToOne.hashCode() + r.left.hashCode() + r.right.className.hashCode();
    }
    private static int hash_onetomany(final Relation r) {
        return  RelationType.OneToMany.hashCode() + r.left.hashCode() + r.right.hashCode();
    }
    private static int hash_manytomany(final Relation r) {
        //scenario: many2many is always bidirectional, same as onetoone
        return RelationType.ManyToMany.hashCode() + r.left.hashCode() + r.right.hashCode();
    }
    private static int hash_onetoone(final Relation r) {
        //scenario: unidirectional relations can be upgraded to bidirectional relations, therefore the same hash should be returned
        return RelationType.OneToOne.hashCode() + r.left.hashCode() + r.right.hashCode();
    }
    public static class Relation {
        EntityDesc left;
        EntityDesc right;
        RelationType type;
        boolean bidirectional;
        public Relation(final EntityDesc left, final EntityDesc right, final RelationType type) {
            this.left = left;
            this.right = right;
            this.type = type;
            if( left.fieldName != null && right.fieldName != null) {
                this.bidirectional = true;
            } else {
                this.bidirectional = false;
            }
        }
        @Override
        public boolean equals(Object o) {
            if( !(o instanceof Relation)) return false;
            Relation that = (Relation) o;
            return (that.hashCode() == this.hashCode());
        }
        @Override
        public int hashCode() {
            return hash(this);
        }

        public boolean isBidirectional() {
            return bidirectional;
        }

        @Override
        public String toString() {
            return "\trelationship " + type.name() + " { \n"
                    + "\t\t" + left.toString() + " to " + right.toString() + "\n"
                    + "\t}";
        }
        public String toStringUntabbed() {
            return left.toString() + " to " + right.toString();
        }
    }

    public static class EntityDesc {
        // for example, Owner{car} to Car{owner}
        // Owner is the class name, while car is the fieldname of type Car.
        String className;
        String fieldName;

        public EntityDesc(final String className, final String fieldName) {
            this.className = className.trim();
            this.fieldName = (fieldName == null) ? null : fieldName.trim();
        }
        @Override
        public boolean equals(Object o) {
            if( !(o instanceof EntityDesc)) return false;
            EntityDesc that = (EntityDesc) o;
            return ( that.hashCode() == this.hashCode());
        }
        @Override
        public int hashCode() {
            int result = className.hashCode();
            result = result + (fieldName != null ? fieldName.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return className + (fieldName != null ? " {" + fieldName + "}" : "");
        }
    }
}
