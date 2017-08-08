package io.github.jhipster.jpa2jdl;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import javax.persistence.Entity;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class NewRunner {

    private ReverseJPA2JDL jpa2jdl = new ReverseJPA2JDL();

    @Option(name="--package", required = true)
    private String rootPackage;

    @Option(name="--archive", required = true)
    private String archiveFile;

    @Option(name="--archiveRoot", required = false)
    private String archiveRoot = "/";

    @Option(name="--out", required = true)
    private String outputpath;

    public static void main(String[] args) {
        Arrays.stream(args).forEach(System.out::println);
        final NewRunner app = new NewRunner();

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

    private void run(){

        final Set<Class<?>> entitySubClasses = new HashSet<>();
        final Set<Class<?>> enums = new HashSet<>();

        try (final JarFile jarFile = new JarFile(archiveFile)) {
            Enumeration<JarEntry> e = jarFile.entries();

            final URL[] urls          = { new URL("jar:file:" + archiveFile + "!" + archiveRoot) };
            final URLClassLoader cl   = URLClassLoader.newInstance(urls, NewRunner.class.getClassLoader());

            while (e.hasMoreElements()) {
                final JarEntry je = e.nextElement();
                if(je.isDirectory() || !je.getName().endsWith(".class")){
                    continue;
                }
                // -6 because of .class
                final String className = je.getName()
                    .substring(0,je.getName().length()-6)
                    .substring(17)
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

                    System.out.println(c.getSimpleName());
                } catch (NoClassDefFoundError | ClassNotFoundException er) {
                    er.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // run the job
        final String text = jpa2jdl.generate(entitySubClasses, enums);
        if( outputpath == null) {
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
        try(final OutputStreamWriter output = new OutputStreamWriter(
            new FileOutputStream(fullPath), "UTF-8");
            final BufferedWriter buffered =
                new BufferedWriter(output)) {
            buffered.write(text);
        }
    }
}
