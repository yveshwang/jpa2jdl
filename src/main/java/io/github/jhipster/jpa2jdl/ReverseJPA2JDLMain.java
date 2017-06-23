package io.github.jhipster.jpa2jdl;

import nl.javadude.scannit.Configuration;
import nl.javadude.scannit.Scannit;
import nl.javadude.scannit.scanner.SubTypeScanner;
import nl.javadude.scannit.scanner.TypeAnnotationScanner;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by yveshwang on 23/06/2017.
 */
public class ReverseJPA2JDLMain {
    private static final Logger LOG = LoggerFactory.getLogger(ReverseJPA2JDLMain.class);
    @Option(name="--packageName")
    private String packageName;
    private ReverseJPA2JDL jpa2jdl = new ReverseJPA2JDL();
    public static void main(String[] args) {
        final ReverseJPA2JDLMain app = new ReverseJPA2JDLMain();
        final CmdLineParser parser = new CmdLineParser(app);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
        }
        app.run();
    }

    public void run() {
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

        // scan enum types in entities field type
        final Set<Class<?>> enums = new LinkedHashSet<>();
        for (Class<?> e : entitySubClasses) {
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
        System.out.println(jpa2jdl.generate(entitySubClasses, enums));
    }
}
