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
    public void addTwoUnidirectionalOne2One() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "OneToOne");
        Assert.assertFalse( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class2", "field2", "class1", null, "OneToOne");
        Assert.assertEquals(1, cache.getOneToOne().size());
        Assert.assertTrue( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    @Test
    public void addOneUniOneBidirectionalOne2One() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "OneToOne");
        Assert.assertFalse( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class2", "field2", "class1", "field1", "OneToOne");
        Assert.assertEquals(1, cache.getOneToOne().size());
        Assert.assertTrue( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
    }
    @Test
    public void addOneBiOneUnidirectionalOne2One() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", "field2", "OneToOne");
        Assert.assertTrue( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class2", "field2", "class1", null, "OneToOne");
        Assert.assertEquals(1, cache.getOneToOne().size());
        Assert.assertTrue( Arrays.asList(cache.getOneToOne().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
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
        Assert.assertFalse( Arrays.asList(cache.getManyToMany().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
        cache.addRelation("class2", "field2", "class1", null, "ManyToMany");
        Assert.assertEquals(1, cache.getManyToMany().size());
    }
    @Test
    public void addOneUniOneBidirectionalManyToMany() {
        //adding two identical relations, result remains to be one
        final RelationsCache cache = new RelationsCache();
        cache.addRelation("class1", "field1", "class2", null, "ManyToMany");
        Assert.assertFalse( Arrays.asList(cache.getManyToMany().values().toArray(new RelationsCache.Relation[0])).get(0).isBidirectional());
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

}
