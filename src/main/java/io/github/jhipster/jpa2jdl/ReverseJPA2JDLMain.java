package io.github.jhipster.jpa2jdl;

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
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReverseJPA2JDLMain {
    private static final Logger LOG = LoggerFactory.getLogger(ReverseJPA2JDLMain.class);
    private ReverseJPA2JDL jpa2jdl = new ReverseJPA2JDL();

    @Option(name = "--package", required = true)
    private String rootPackage;

    @Option(name = "--archive", required = true)
    private String archiveFile;

    @Option(name = "--archiveRoot")
    private String archiveRoot = "/";

    @Option(name = "--out")
    private String outputpath;

    public static void main(String[] args) {
        final ReverseJPA2JDLMain app = new ReverseJPA2JDLMain();
        final CmdLineParser parser = new CmdLineParser(app);
        try {
            parser.parseArgument(args);
        } catch (final CmdLineException e) {
            // handling of wrong arguments
            LOG.error("Error while processing program arguments", e);
            parser.printUsage(System.err);
            System.exit(1);
        }
        app.run();
    }

    private void run() {
        final Set<Class<?>> entitySubClasses = new HashSet<>();
        final Set<Class<?>> enums = new HashSet<>();

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
                    .substring(archiveRoot.length()-1)
                    .replace('/', '.');

                if (!(className.startsWith(rootPackage))) {
                    continue;
                }

                try {
                    final Class c = cl.loadClass(className);
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
