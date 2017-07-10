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
    @BeforeClass
    public static void parse() {
        final ReverseJPA2JDL parser = new ReverseJPA2JDL();
        final StringBuilder entityBuilder = new StringBuilder();
        parser.generateClass2Jdl(SimpleEntity.class, entityBuilder);
        entityOutput = entityBuilder.toString();
        System.out.println(entityOutput);
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
        Assert.assertTrue(entityOutput.contains("min5 Integer required min(4)"));
        Assert.assertTrue(entityOutput.contains("min6 AnyBlob required minbytes(4)"));
    }
    @Test
    public void maxFieldTest() {
        Assert.assertTrue(entityOutput.contains("max1 String maxlength(2)"));
        Assert.assertTrue(entityOutput.contains("max2 String required maxlength(2)"));
        Assert.assertTrue(entityOutput.contains("max3 String required maxlength(2)"));
        Assert.assertTrue(entityOutput.contains("max4 Integer required max(4)"));
        Assert.assertTrue(entityOutput.contains("max5 AnyBlob required maxbytes(4)"));
    }
    @Test
    public void fieldnameTest() {
        Assert.assertTrue(entityOutput.contains("fieldname1 String"));
        Assert.assertTrue(entityOutput.contains("fieldname2 String"));
    }
    @Test
    public void blogTest(){
        Assert.assertTrue(entityOutput.contains("contentblob AnyBlob required minbytes(2)"));
        Assert.assertTrue(entityOutput.contains("imageblob AnyBlob"));
        Assert.assertTrue(entityOutput.contains("textblob TextBlob"));
    }
    @Test
    public void patternTest() {
        Assert.assertTrue(entityOutput.contains("email String required minlength(5) maxlength(30) pattern(\"[\\\\w]*@[a-zA-Z]*.com\")"));
    }
}
