package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.ReverseJPA2JDL;
import io.github.jhipster.jpa2jdl.example.entities.complex.MoneyEntity;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JPAMoneyTests {

    private static String entityOutput;

    @BeforeClass
    public static void parse() {
        final ReverseJPA2JDL parser = new ReverseJPA2JDL();
        final StringBuilder entityBuilder = new StringBuilder();
        parser.generateClass2Jdl(MoneyEntity.class, entityBuilder);
        entityOutput = entityBuilder.toString();
        System.out.println(entityOutput);
    }

    @Test
    public void embeddedEntityTest(){
        Assert.assertTrue( entityOutput.contains("originalCurrency String minlength(3) maxlength(3)"));
        Assert.assertTrue( entityOutput.contains("originalValue BigDecimal"));
        Assert.assertTrue( entityOutput.contains("pumpPriceCurrency String minlength(3) maxlength(3)"));
        Assert.assertTrue( entityOutput.contains("pumpPriceValue BigDecimal"));
        Assert.assertTrue( entityOutput.contains("embeddedMoneyEmbeddableCurrency String minlength(3) maxlength(3)"));
        Assert.assertTrue( entityOutput.contains("embeddedMoneyEmbeddableValue BigDecimal"));
        Assert.assertTrue( entityOutput.contains("embeddablePriceCurrency String minlength(3) maxlength(3)"));
        Assert.assertTrue( entityOutput.contains("embeddablePriceValue BigDecimal"));
    }
}
