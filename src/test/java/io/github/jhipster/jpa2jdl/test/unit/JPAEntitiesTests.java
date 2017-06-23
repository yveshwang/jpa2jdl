package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.ReverseJPA2JDL;
import io.github.jhipster.jpa2jdl.example.entities.SimpleEntity;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by yveshwang on 22/06/2017.
 */
public class JPAEntitiesTests {
    private static String entityOutput;
    private static String relationsOutput;
    @BeforeClass
    public static void parse() {
        final ReverseJPA2JDL parser = new ReverseJPA2JDL();
        final StringBuilder entityBuilder = new StringBuilder();
        final StringBuilder relationsBuilder = new StringBuilder();
        parser.generateClass2Jdl(entityBuilder, relationsBuilder, SimpleEntity.class);
        entityOutput = entityBuilder.toString();
        relationsOutput = relationsBuilder.toString();
        System.out.println(entityOutput);
        System.out.println(relationsOutput);
    }
    @Test
    public void simpleEntityTest(){
        Assert.assertTrue( entityOutput.contains("text String"));
        Assert.assertTrue( entityOutput.contains("blah int"));
    }
    @Test
    public void skipIdTest(){
        Assert.assertFalse( entityOutput.contains("id Long"));
    }
    @Test
    public void requiredFieldTests() {
        Assert.assertTrue(entityOutput.contains("postcode String required"));
        //required
        Assert.assertTrue(entityOutput.contains("notnull1 Integer required"));
        Assert.assertTrue(entityOutput.contains("notnull2 Integer required"));
        Assert.assertTrue(entityOutput.contains("notnull3 Integer required"));
        Assert.assertTrue(entityOutput.contains("notnull4 Integer required"));

        //not required
        Assert.assertFalse(entityOutput.contains("null1 Double required"));
        Assert.assertFalse(entityOutput.contains("null2 Double required"));
    }
    @Test
    public void minFieldTest(){
        Assert.assertTrue(entityOutput.contains("min1 String minlength(1)"));
        Assert.assertTrue(entityOutput.contains("min2 String required minlength(6)"));
        Assert.assertTrue(entityOutput.contains("min3 String required minlength(1)"));
        Assert.assertTrue(entityOutput.contains("min4 String required minlength(4)"));
    }
    @Test
    public void maxFieldTest() {
        Assert.assertTrue(entityOutput.contains("max1 String maxlength(2)"));
        Assert.assertTrue(entityOutput.contains("max2 String required maxlength(2)"));
        Assert.assertTrue(entityOutput.contains("max3 String required maxlength(2)"));
    }
    @Test
    public void fieldnameTest() {

    }
    @Test
    public void blogTest(){
        System.out.println("not supported for now");
    }
    @Test
    public void joingViaID(){

    }
}
