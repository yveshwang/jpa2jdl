package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.test.unit.entities.SimpleEntity;
import io.github.jhipster.jpa2jdl.ReverseJPA2JDLMain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by yveshwang on 22/06/2017.
 */
public class JDLOptionTests {
    private final ReverseJPA2JDLMain parser = new ReverseJPA2JDLMain();
    private StringBuilder builder = new StringBuilder();
    @Before
    public void setup() {
        this.builder.delete(0, builder.length());
    }
    @Test
    public void paginationTest() {
        parser.generatePagination(builder, SimpleEntity.class);
        Assert.assertTrue(builder.toString().contains("pager"));

    }
    @Test
    public void serviceTest() {
        parser.generateServices(builder, SimpleEntity.class);
        Assert.assertTrue(builder.toString().contains("serviceClass"));
    }
    @Test
    public void dtoTest(){
        parser.generateDto(builder, SimpleEntity.class);
        Assert.assertTrue(builder.toString().contains("mapstruct"));
    }
}
