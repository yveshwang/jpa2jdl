package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.RelationsCache;
import io.github.jhipster.jpa2jdl.RelationsCache.EntityDesc;
import io.github.jhipster.jpa2jdl.RelationsCache.Relation;
import io.github.jhipster.jpa2jdl.ReverseJPA2JDL;
import io.github.jhipster.jpa2jdl.ReverseJPA2JDLMain;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by yveshwang on 26/06/2017.
 */
public class JPARelationsTests {

    private static final String TEST_PACKAGE_NAME = "io.github.jhipster.jpa2jdl.example.entities.relations";
    private static Set<Class<?>> CLASSES;
    private static Set<Class<?>> ENUMS;
    private static String OUTPUT;


    @BeforeClass
    public static void parse() {
        CLASSES = ReverseJPA2JDLMain.packageNameToClasses(TEST_PACKAGE_NAME);
        ENUMS = ReverseJPA2JDLMain.filterOutEnums(CLASSES);
        final StringBuilder builder = new StringBuilder();
        final ReverseJPA2JDL jpa2jdp = new ReverseJPA2JDL();
        OUTPUT = jpa2jdp.generate(CLASSES, ENUMS);
        System.out.println(OUTPUT);
    }

    //@Test
    //public void
}
