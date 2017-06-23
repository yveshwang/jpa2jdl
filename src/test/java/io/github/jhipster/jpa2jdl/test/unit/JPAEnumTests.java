package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.ReverseJPA2JDL;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by yveshwang on 22/06/2017.
 */
public class JPAEnumTests {
    enum TestEnum1 {
        CAT, DOG;
    }
    enum TestEnum2 {
        ONE(1), TWO(2);
        private final int value;
        TestEnum2(final int val) {
            this.value = val;
        }
        int value(){
            return this.value;
        }
    }

    private <T extends Enum<T>> void parseEnum(Class<T> clazz) {
        final ReverseJPA2JDL parser = new ReverseJPA2JDL();
        final StringBuilder builder = new StringBuilder();
        parser.generateEnum2Jdl(builder, clazz);
        final String output = builder.toString();
        Arrays.stream(clazz.getEnumConstants()).forEach(value -> {
            Assert.assertTrue( output.contains(value.name()));
        } );
    }
    @Test
    public void readAndParseTestEnum1(){
        parseEnum(TestEnum1.class);
    }
    @Test
    public void readAndParseTestEnum2() {
        parseEnum(TestEnum2.class);
    }
}
