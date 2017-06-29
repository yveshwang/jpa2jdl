package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.RelationsCache;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by yveshwang on 28/06/2017.
 */
public class JPARelationsCacheTests {
    // 1-1 tests
    @Test
    public void addIdenticalOne2One() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("leftClass", "leftField", "rightClass", "rightField", "OneToOne");
        cache.addRelation("leftClass", "leftField", "rightClass", "rightField", "OneToOne");
        Assert.assertEquals(1, cache.getOneToOne().size());
    }
    @Test
    public void addTwoUnidirectionalOne2One_related() {
        //adding two inverse relations, we cant make assumptions but we will just pile them on
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "OneToOne");
        Assert.assertFalse( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class2", "field2", "class1", null, "OneToOne");
        Assert.assertEquals(2, cache.getOneToOne().size());
        Assert.assertFalse( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    @Test
    public void addOneUniOneBidirectionalOne2One_related() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "OneToOne");
        Assert.assertFalse( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class2", "field2", "class1", "field1", "OneToOne");
        Assert.assertEquals(1, cache.getOneToOne().size());
        Assert.assertTrue( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    @Test
    public void addOneBiOneUnidirectionalOne2One_related() {
        //adding 1 bi and 1 inverse relations, result remains to be one bi
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", "field2", "OneToOne");
        Assert.assertTrue( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class2", "field2", "class1", null, "OneToOne");
        Assert.assertEquals(1, cache.getOneToOne().size());
        Assert.assertTrue( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    @Test
    public void addTwoUnidirectionalOne2One_differonfield() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "OneToOne");
        Assert.assertFalse( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class1", "field2", "class2", null, "OneToOne");
        Assert.assertEquals(2, cache.getOneToOne().size());
    }
    @Test
    public void addTwoUnidirectionalOne2One_differonfieldAndClass() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "OneToOne");
        Assert.assertFalse( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class3", "field2", "class2", null, "OneToOne");
        Assert.assertEquals(2, cache.getOneToOne().size());
    }
    @Test
    public void addOneUniOneBidirectionalOne2One_differonfield() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "OneToOne");
        Assert.assertFalse( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class1", "field2", "class1", "field3", "OneToOne");
        Assert.assertEquals(2, cache.getOneToOne().size());
    }
    @Test
    public void addOneBiOneUnidirectionalOne2One_differonfieldAndClass() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", "field2", "OneToOne");
        Assert.assertTrue( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class3", "field3", "class4", null, "OneToOne");
        Assert.assertEquals(2, cache.getOneToOne().size());
    }
    // -----

    // many-to-many tests
    @Test
    public void addIdenticalManyToMany() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("leftClass", "leftField", "rightClass", "rightField", "ManyToMany");
        cache.addRelation("leftClass", "leftField", "rightClass", "rightField", "ManyToMany");
        Assert.assertEquals(1, cache.getManyToMany().size());
    }
    @Test
    public void addTwoUnidirectionalManyToMany() {
        //adding two identical relations, result remains to be one
        // but this is an erroneous situation as no JPA entity will own the relation.
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "ManyToMany");
        Assert.assertEquals(0, cache.getManyToMany().size());
        cache.addRelation("class2", "field2", "class1", null, "ManyToMany");
        Assert.assertEquals(0, cache.getManyToMany().size());
    }
    @Test
    public void addOneUniOneBidirectionalManyToMany() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "ManyToMany");
        Assert.assertEquals(0, cache.getManyToMany().size());
        cache.addRelation("class2", "field2", "class1", "field1", "ManyToMany");
        Assert.assertEquals(1, cache.getManyToMany().size());
        Assert.assertTrue( Arrays.asList(cache.getManyToMany().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    @Test
    public void addOneBiOneUnidirectionalManyToMany() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", "field2", "ManyToMany");
        Assert.assertTrue( Arrays.asList(cache.getManyToMany().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class2", "field2", "class1", null, "ManyToMany");
        Assert.assertEquals(1, cache.getManyToMany().size());
        Assert.assertTrue( Arrays.asList(cache.getManyToMany().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    // -----
    // many-to-one tests
    @Test
    public void addIdenticalManyToOne() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("leftClass", "leftField", "rightClass", null, "ManyToOne");
        cache.addRelation("leftClass", "leftField", "rightClass", null, "ManyToOne");
        Assert.assertEquals(1, cache.getManyToOne().size());
    }
    @Test
    public void addTwoUnidirectionalManyToOne() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "ManyToOne");
        Assert.assertEquals(1, cache.getManyToOne().size());
        cache.addRelation("class2", "field2", "class1", null, "ManyToOne");
        Assert.assertEquals(2, cache.getManyToOne().size());
    }
    @Test
    public void addOneUniOneBidirectionalManyToOne() {
        //adding two identical relations, result remains to be one
        // but this is an erroneous situation.
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "ManyToOne");
        Assert.assertEquals(1, cache.getManyToOne().size());
        cache.addRelation("class2", "field2", "class1", "field1", "ManyToOne");
        Assert.assertEquals(1, cache.getManyToOne().size());
        Assert.assertFalse( Arrays.asList(cache.getManyToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    @Test
    public void addOneBiOneUnidirectionalManyToOne() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", "field2", "ManyToOne");
        Assert.assertEquals(0, cache.getManyToOne().size());
        cache.addRelation("class2", "field2", "class1", null, "ManyToOne");
        Assert.assertEquals(1, cache.getManyToOne().size());
        Assert.assertFalse( Arrays.asList(cache.getManyToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    @Test
    public void addOneBiOneToManyUniManyToOne_redundantManyToOne(){
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", "field2", "OneToMany");
        Assert.assertEquals(1, cache.getOneToMany().size());
        cache.addRelation("class2", "field2", "class1", null, "ManyToOne");
        Assert.assertEquals(1, cache.getOneToMany().size());
        Assert.assertEquals(0, cache.getManyToOne().size());
    }
    @Test
    public void addOneBiOneToManyUniManyToOne(){
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", "field2", "OneToMany");
        Assert.assertEquals(1, cache.getOneToMany().size());
        cache.addRelation("class1", "field2", "class2", null, "ManyToOne");
        Assert.assertEquals(1, cache.getOneToMany().size());
        Assert.assertEquals(1, cache.getManyToOne().size());
    }
    // -----
    // one-to-many tests
    @Test
    public void addIdenticalOneToMany() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("leftClass", "leftField", "rightClass", "rightField", "OneToMany");
        cache.addRelation("leftClass", "leftField", "rightClass", "rightField", "OneToMany");
        Assert.assertEquals(1, cache.getOneToMany().size());
    }
    @Test
    public void addTwoUnidirectionalOneToMany() {
        //adding two identical relations, result remains to be one
        // but this is an erroneous situation.
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "OneToMany");
        Assert.assertEquals(0, cache.getOneToMany().size());
        cache.addRelation("class2", "field2", "class1", null, "OneToMany");
        Assert.assertEquals(0, cache.getOneToMany().size());
    }
    @Test
    public void addOneUniOneBidirectionalOneToMany() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "OneToMany");
        Assert.assertEquals(0, cache.getOneToMany().size());
        cache.addRelation("class2", "field2", "class1", "field1", "OneToMany");
        Assert.assertEquals(1, cache.getOneToMany().size());
        Assert.assertTrue( Arrays.asList(cache.getOneToMany().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    @Test
    public void addOneBiOneUnidirectionalOneToMany() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", "field2", "OneToMany");
        Assert.assertTrue( Arrays.asList(cache.getOneToMany().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class2", "field2", "class1", null, "OneToMany");
        Assert.assertEquals(1, cache.getOneToMany().size());
        Assert.assertTrue( Arrays.asList(cache.getOneToMany().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }

    // -----
}
