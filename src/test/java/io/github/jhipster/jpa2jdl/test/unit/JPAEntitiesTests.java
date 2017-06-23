package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.test.unit.entities.SimpleEntity;
import io.github.jhipster.jpa2jdl.ReverseJPA2JDLMain;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yveshwang on 22/06/2017.
 */
public class JPAEntitiesTests {
    @Test
    public void simpleEntityTest(){
        final ReverseJPA2JDLMain parser = new ReverseJPA2JDLMain();
        final StringBuilder entityBuilder = new StringBuilder();
        final StringBuilder relationsBuilder = new StringBuilder();
        parser.generateClass2Jdl(entityBuilder, relationsBuilder, SimpleEntity.class);
        final String entityOutput = entityBuilder.toString();
        final String relationsOutput = relationsBuilder.toString();
        Assert.assertTrue( entityOutput.contains("text String"));
        Assert.assertTrue( entityOutput.contains("blah int"));
        Assert.assertTrue( entityOutput.contains("id Long"));
        System.out.println(entityOutput);
        System.out.println(relationsOutput);
    }
}
