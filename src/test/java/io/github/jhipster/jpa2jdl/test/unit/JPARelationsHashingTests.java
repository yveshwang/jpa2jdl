package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.RelationsCache;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yveshwang on 28/06/2017.
 */
public class JPARelationsHashingTests {
    private final RelationsCache.EntityDesc left = new RelationsCache.EntityDesc("LeftClass", "leftfield");
    private final RelationsCache.EntityDesc right = new RelationsCache.EntityDesc("RightClass", "rightField");
    private final RelationsCache.EntityDesc uniright = new RelationsCache.EntityDesc("RightClass", null);
    @Test
    public void entityDescTest() {
        final RelationsCache.EntityDesc left = new RelationsCache.EntityDesc("LeftClass", "leftfield");
        final RelationsCache.EntityDesc left1 = new RelationsCache.EntityDesc("LeftClass", "leftfield");
        final RelationsCache.EntityDesc left2 = new RelationsCache.EntityDesc("LeftClass2", "leftfield2");
        Assert.assertEquals(left, left1);
        Assert.assertNotEquals(left, left2);
    }
    @Test
    public void one2onehashingtest_bidirectional() {
        final RelationsCache.Relation relation1 = new RelationsCache.Relation(left, right, RelationsCache.RelationType.OneToOne);
        final RelationsCache.Relation relation2 = new RelationsCache.Relation(right, left, RelationsCache.RelationType.OneToOne);
        Assert.assertEquals(relation1, relation2);
    }
    @Test
    public void one2onehashingtest_unidirection() {
        final RelationsCache.Relation relation1 = new RelationsCache.Relation(left, uniright, RelationsCache.RelationType.OneToOne);
        final RelationsCache.Relation relation2 = new RelationsCache.Relation(uniright, left, RelationsCache.RelationType.OneToOne);
        Assert.assertEquals(relation1, relation2);
    }
    @Test
    public void many2manyhashingtest() {
        final RelationsCache.Relation relation1 = new RelationsCache.Relation(left, right, RelationsCache.RelationType.ManyToMany);
        final RelationsCache.Relation relation2 = new RelationsCache.Relation(right, left, RelationsCache.RelationType.ManyToMany);
        Assert.assertEquals(relation1, relation2);
    }
    @Test
    public void many2manyhashingtest_ignore_unidirection() {
        final RelationsCache.Relation relation1 = new RelationsCache.Relation(left, uniright, RelationsCache.RelationType.ManyToMany);
        final RelationsCache.Relation relation2 = new RelationsCache.Relation(uniright, left, RelationsCache.RelationType.ManyToMany);
        Assert.assertEquals(relation1, relation2);
        System.out.println(RelationsCache.RelationType.ManyToMany.hashCode());
        System.out.println(RelationsCache.RelationType.OneToMany.hashCode());
        System.out.println(RelationsCache.RelationType.ManyToOne.hashCode());
        System.out.println(RelationsCache.RelationType.OneToOne.hashCode());
    }
    @Test
    public void one2many_test() {
        final RelationsCache.Relation relation1 = new RelationsCache.Relation(left, uniright, RelationsCache.RelationType.OneToMany);
        final RelationsCache.Relation relation2 = new RelationsCache.Relation(uniright, left, RelationsCache.RelationType.OneToMany);
        final RelationsCache.Relation relation3 = new RelationsCache.Relation(left, uniright, RelationsCache.RelationType.ManyToOne);
        final RelationsCache.Relation relation4 = new RelationsCache.Relation(uniright, left, RelationsCache.RelationType.ManyToOne);
        final RelationsCache.Relation relation5 = new RelationsCache.Relation(left, uniright, RelationsCache.RelationType.ManyToMany);
        final RelationsCache.Relation relation6 = new RelationsCache.Relation(right, left, RelationsCache.RelationType.OneToOne);
        Assert.assertEquals(relation1, relation2);
        Assert.assertEquals(relation1, relation3);
        Assert.assertEquals(relation1, relation4);
        Assert.assertEquals(relation2, relation3);
        Assert.assertEquals(relation2, relation4);
        Assert.assertEquals(relation3, relation4);
        Assert.assertNotEquals(relation1, relation5);
        Assert.assertNotEquals(relation1, relation6);
        Assert.assertNotEquals(relation5, relation6);
    }
}
