package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.ReverseJPA2JDL;
import io.github.jhipster.jpa2jdl.example.entities.complex.EmbeddedEntity;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JPAEmbeddedTests {

    private static String entityOutput;

    @BeforeClass
    public static void parse() {
        final ReverseJPA2JDL parser = new ReverseJPA2JDL();
        final StringBuilder entityBuilder = new StringBuilder();
        parser.generateClass2Jdl(EmbeddedEntity.class, entityBuilder);
        entityOutput = entityBuilder.toString();
        System.out.println(entityOutput);
    }

    @Test
    public void embeddedEntityTest(){
        Assert.assertTrue( entityOutput.contains("embeddedEntityLabel String"));
        Assert.assertTrue( entityOutput.contains("embeddedEntityNumber Integer"));
        Assert.assertTrue( entityOutput.contains("embeddedEntity2Label String"));
        Assert.assertTrue( entityOutput.contains("embeddedEntity2Number Integer"));

        Assert.assertTrue( entityOutput.contains("complexEntityAmount BigDecimal"));
        Assert.assertTrue( entityOutput.contains("complexEntityInnerEntityLabel String"));
        Assert.assertTrue( entityOutput.contains("complexEntityInnerEntityNumber Integer"));
        Assert.assertTrue( entityOutput.contains("complexEntityAmountLabel String"));
    }
}
