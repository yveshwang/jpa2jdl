package io.github.jhipster.jpa2jdl.test.unit;

import io.github.jhipster.jpa2jdl.ReverseJPA2JDL;
import nl.javadude.scannit.Configuration;
import nl.javadude.scannit.Scannit;
import nl.javadude.scannit.scanner.SubTypeScanner;
import nl.javadude.scannit.scanner.TypeAnnotationScanner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by yveshwang on 26/06/2017.
 */
public class JPARelationsTests {

    private static final Logger LOG = LoggerFactory.getLogger(JPARelationsTests.class);
    private static final String TEST_PACKAGE_NAME = "io.github.jhipster.jpa2jdl.example.entities.relations";
    private static Set<Class<?>> CLASSES;
    private static Set<Class<?>> ENUMS;
    private static String OUTPUT;

    @BeforeClass
    public static void parse() {
        CLASSES = readClasses(TEST_PACKAGE_NAME);
        ENUMS = readEnums(CLASSES);
        //final StringBuilder builder = new StringBuilder();
        final ReverseJPA2JDL jpa2jdp = new ReverseJPA2JDL();
        OUTPUT = jpa2jdp.generate(CLASSES, ENUMS);
        System.out.println(OUTPUT);
        System.out.println(jpa2jdp.debuginfo());
    }

    @Test
    public void test(){
        System.out.println("boom");
    }


    private static Set<Class<?>> readClasses(final String packageName) {
        final Configuration config = Configuration.config()
            .with(new SubTypeScanner(), new TypeAnnotationScanner())
            .scan(packageName);
        final Scannit scannit = new Scannit(config);
        final Set<Class<?>> entityClasses = scannit.getTypesAnnotatedWith(Entity.class);
        final Set<Class<?>> entitySubClasses = new HashSet<>();
        entitySubClasses.addAll(entityClasses);
        for (final Class<?> e : entityClasses) {
            final Set<Class<?>> subClasses = (Set<Class<?>>) scannit.getSubTypesOf(e);
            if (subClasses != null && !subClasses.isEmpty()) {
                entitySubClasses.addAll(subClasses);
            }
        }

        LOG.info("Found @Entity classes:" + entityClasses.size() + " : " + entityClasses);
        if (entitySubClasses.size() > entityClasses.size()) {
            LOG.info("Found sub-classes of @Entity classes:" + entitySubClasses.size() + " : " + entitySubClasses);
        }
        return entitySubClasses;
    }

    private static Set<Class<?>> readEnums(final Set<Class<?>> classesToFilter) {
        // scan enum types in entities field type
        final Set<Class<?>> enums = new LinkedHashSet<>();
        for (Class<?> e : classesToFilter) {
            final Field[] declaredFields = e.getDeclaredFields();
            for (final Field f : declaredFields) {
                final Class<?> fieldType = f.getType();
                if (fieldType.isEnum()) {
                    if (f.isSynthetic() || Modifier.isStatic(f.getModifiers())) {
                        continue;
                    }
                    enums.add(fieldType);
                }
            }
        }
        return enums;
    }
}
