package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.RelationsCache;
import io.github.jhipster.jpa2jdl.RelationsCache.EntityDesc;
import io.github.jhipster.jpa2jdl.RelationsCache.Relation;
import io.github.jhipster.jpa2jdl.ReverseJPA2JDL;
import io.github.jhipster.jpa2jdl.ReverseJPA2JDLMain;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

/**
 * Created by yveshwang on 26/06/2017.
 */
public class JPARelationsTests {

    private static final String TEST_PACKAGE_NAME = "io.github.jhipster.jpa2jdl.example.entities.relations";
    private static Set<Class<?>> CLASSES;
    private static Set<Class<?>> ENUMS;
    private static String OUTPUT;

    private final EntityDesc left = new EntityDesc("LeftClass", "leftfield");
    private final EntityDesc right = new EntityDesc("RightClass", "rightField");
    private final EntityDesc uniright = new EntityDesc("RightClass", null);
    @BeforeClass
    public static void parse() {
        CLASSES = ReverseJPA2JDLMain.packageNameToClasses(TEST_PACKAGE_NAME);
        ENUMS = ReverseJPA2JDLMain.filterOutEnums(CLASSES);
        final StringBuilder builder = new StringBuilder();
        final ReverseJPA2JDL jpa2jdp = new ReverseJPA2JDL();
        OUTPUT = jpa2jdp.generate(CLASSES, ENUMS);
        System.out.println(OUTPUT);
    }
    @Test
    public void entityDescTest() {
        final EntityDesc left = new EntityDesc("LeftClass", "leftfield");
        final EntityDesc left1 = new EntityDesc("LeftClass", "leftfield");
        final EntityDesc left2 = new EntityDesc("LeftClass2", "leftfield2");
        Assert.assertEquals(left, left1);
        Assert.assertNotEquals(left, left2);
    }
    @Test
    public void one2onehashingtest_bidirectional() {
        final Relation relation1 = new Relation(left, right, RelationsCache.RelationType.OneToOne);
        final Relation relation2 = new Relation(right, left, RelationsCache.RelationType.OneToOne);
        Assert.assertEquals(relation1, relation2);
    }
    @Test
    public void one2onehashingtest_unidirection() {
        final Relation relation1 = new Relation(left, uniright, RelationsCache.RelationType.OneToOne);
        final Relation relation2 = new Relation(uniright, left, RelationsCache.RelationType.OneToOne);
        Assert.assertEquals(relation1, relation2);
    }
    @Test
    public void many2manyhashingtest() {
        final Relation relation1 = new Relation(left, right, RelationsCache.RelationType.ManyToMany);
        final Relation relation2 = new Relation(right, left, RelationsCache.RelationType.ManyToMany);
        Assert.assertEquals(relation1, relation2);
    }
    @Test
    public void many2manyhashingtest_ignore_unidirection() {
        final Relation relation1 = new Relation(left, uniright, RelationsCache.RelationType.ManyToMany);
        final Relation relation2 = new Relation(uniright, left, RelationsCache.RelationType.ManyToMany);
        Assert.assertEquals(relation1, relation2);
        System.out.println(RelationsCache.RelationType.ManyToMany.hashCode());
        System.out.println(RelationsCache.RelationType.OneToMany.hashCode());
        System.out.println(RelationsCache.RelationType.ManyToOne.hashCode());
        System.out.println(RelationsCache.RelationType.OneToOne.hashCode());
    }
    @Test
    public void one2many_test() {
        final Relation relation1 = new Relation(left, uniright, RelationsCache.RelationType.OneToMany);
        final Relation relation2 = new Relation(uniright, left, RelationsCache.RelationType.OneToMany);
        final Relation relation3 = new Relation(left, uniright, RelationsCache.RelationType.ManyToOne);
        final Relation relation4 = new Relation(uniright, left, RelationsCache.RelationType.ManyToOne);
        final Relation relation5 = new Relation(left, uniright, RelationsCache.RelationType.ManyToMany);
        final Relation relation6 = new Relation(right, left, RelationsCache.RelationType.OneToOne);
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
    @Test
    public void addIdenticalOne2One() {
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("leftClass", "leftField", "rightClass", "rightField", "OneToOne");
        cache.addRelation("leftClass", "leftField", "rightClass", "rightField", "OneToOne");
        Assert.assertEquals(1, cache.getOneToOne().size());
    }
}
