package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.example.entities.SimpleEntity;
import io.github.jhipster.jpa2jdl.ReverseJPA2JDL;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yveshwang on 22/06/2017.
 */
public class JPAEntitiesTests {
    @Test
    public void simpleEntityTest(){
        final ReverseJPA2JDL parser = new ReverseJPA2JDL();
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
