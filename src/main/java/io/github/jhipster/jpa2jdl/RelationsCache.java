package io.github.jhipster.jpa2jdl;

import java.util.HashMap;

/**
 * Created by yveshwang on 26/06/2017.
 */
public class RelationsCache {
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
                // scenario one_to_one 1: if a bidirection relatinos is already added, then unidirection is ignored.
                // scenario one_to_one 2: if unidirection is already added and another unidirection is being added of the opposite direction, then upgrade to bidirectional
                // scenario one_to_one 3: if uni is already added, and adding another bidirection, then upgrade to bidirectional
                if( oneToOne.containsKey(Integer.valueOf(r.hashCode()))) {
                    final RelationsCache.Relation existing = oneToOne.get(Integer.valueOf(r.hashCode()));
                    if( !existing.bidirectional) {
                        if( (!r.bidirectional &&
                                (existing.left.hashCode() == r.right.hashCode())) ||
                                (r.bidirectional)) {
                            r.bidirectional = true;
                            oneToOne.put(Integer.valueOf(r.hashCode()), r);
                        }
                    }
                } else {
                    //does not contain the key, let's add it
                    oneToOne.put(Integer.valueOf(r.hashCode()), r);
                }
            case OneToMany:
                break;
            case ManyToOne:
                break;
            case ManyToMany:
                break;
            default:

        }
        //scenario many_to_many 2: ignore unidirection because it makes no sense, and assume all are bidirectional.
        //scenario many_to_one 3: many_to_one could be bidrectional as that would be the same as one_to_many

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
                return hash_onetomany_or_manytoone(r);
            case ManyToOne:
                return hash_onetomany_or_manytoone(r);
            case ManyToMany:
                return hash_manytomany(r);
        }
        return -1;
    }
    private static int hash_onetomany_or_manytoone(final Relation r) {
        return  RelationType.ManyToOne.hashCode() + RelationType.OneToMany.hashCode() + r.left.hashCode() + r.right.hashCode();
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
            Relation that = (Relation) o;
            return (that.hashCode() == this.hashCode());
        }
        @Override
        public int hashCode() {
            return hash(this);
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
            EntityDesc that = (EntityDesc) o;
            return ( that.hashCode() == this.hashCode());
        }
        @Override
        public int hashCode() {
            int result = className.hashCode();
            result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
            return result;
        }
    }
}
