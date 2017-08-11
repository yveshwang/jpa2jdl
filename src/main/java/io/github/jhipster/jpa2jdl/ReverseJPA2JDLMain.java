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
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReverseJPA2JDLMain {
    private static final Logger LOG = LoggerFactory.getLogger(ReverseJPA2JDLMain.class);
    private ReverseJPA2JDL jpa2jdl = new ReverseJPA2JDL();

    @Option(name = "--package", required = true, usage = "Specify the package name to scan for classes annoated with @Entity.", metaVar = "io.github.jhipster")
    private String rootPackage;

    @Option(name = "--archive", required = false, usage = "Specify the a single explicit jar for scanning.", metaVar = "/some/dir/to/archive.jar")
    private String archiveFile;

    @Option(name = "--archiveRoot", required = false, usage = "Specify the root of the classes within an executable jar for example.", metaVar = "/BOOT-INF/classes/")
    private String archiveRoot = "/";

    @Option(name = "--out", required = false, usage = "Specify the output JDL file to be written.", metaVar = "\"~/example.jh\"")
    private String outputpath;

    public static void main(String[] args) {
        final ReverseJPA2JDLMain app = new ReverseJPA2JDLMain();
        final CmdLineParser parser = new CmdLineParser(app);
        try {
            parser.parseArgument(args);
        } catch (final CmdLineException e) {
            // handling of wrong arguments
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }
        app.run();
    }

    public static Set<Class<?>> packageNameToClasses(final String name) {
        final Configuration config = Configuration.config()
            .with(new SubTypeScanner(), new TypeAnnotationScanner())
            .scan(name);
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

    public static Set<Class<?>> filterOutEnums(Set<Class<?>> entitySubClasses) {
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
        return enums;
    }

    private void run() {
        if (Objects.isNull(archiveFile)) {
            final Set<Class<?>> entitySubClasses = packageNameToClasses(rootPackage);
            final Set<Class<?>> enums = filterOutEnums(entitySubClasses);


            generate(entitySubClasses, enums);
        } else {
            final Set<Class<?>> entitySubClasses = new HashSet<>();
            final Set<Class<?>> enums = new HashSet<>();

            collectClassesFromArchiveFile(entitySubClasses, enums);
            generate(entitySubClasses, enums);
        }
    }

    private void collectClassesFromArchiveFile(Set<Class<?>> entitySubClasses, Set<Class<?>> enums) {
        try (final JarFile jarFile = new JarFile(archiveFile)) {
            final Enumeration<JarEntry> entry = jarFile.entries();
            final URL[] urls = {new URL("jar:file:" + archiveFile + "!" + archiveRoot)};
            final URLClassLoader cl = URLClassLoader.newInstance(urls, ReverseJPA2JDLMain.class.getClassLoader());

            while (entry.hasMoreElements()) {
                final JarEntry je = entry.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                final String className = je.getName()
                    .substring(0, je.getName().length() - 6)
                    // because of archiveRoot
                    .substring(archiveRoot.length() - 1)
                    .replace('/', '.');

                if (!(className.startsWith(rootPackage))) {
                    continue;
                }

                try {
                    final Class<?> c = cl.loadClass(className);
                    if (c.isEnum()) {
                        enums.add(c);
                    }

                    final Annotation entityAnnotation = c.getDeclaredAnnotation(Entity.class);
                    if (!Objects.isNull(entityAnnotation)) {
                        entitySubClasses.add(c);
                    }

                    LOG.debug("Processed {} class", c.getSimpleName());
                } catch (final NoClassDefFoundError | ClassNotFoundException er) {
                    LOG.error("Cannot load class", er);
                }
            }
        } catch (final IOException e) {
            LOG.error("Cannot read from archive", e);
        }
    }

    private void generate(final Set<Class<?>> entitySubClasses, final Set<Class<?>> enums) {
        // run the job
        final String text = jpa2jdl.generate(entitySubClasses, enums);
        if (outputpath == null) {
            System.out.println(text);
        } else {
            try {
                write(text, outputpath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(final String text, final String fullPath) throws IOException {
        try (final OutputStreamWriter output = new OutputStreamWriter(
            new FileOutputStream(fullPath), "UTF-8");
             final BufferedWriter buffered =
                 new BufferedWriter(output)) {
            buffered.write(text);
        }
    }
}
