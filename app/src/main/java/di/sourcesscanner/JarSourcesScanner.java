package di.sourcesscanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarSourcesScanner extends AbstractSourcesScanner {

    private final String directory;
    private final File codeSourceFile;

    public JarSourcesScanner(String directory, File codeSourceFile) {
        this.directory = directory;
        this.codeSourceFile = codeSourceFile;
    }

    @Override
    protected Set<String> getClassFileNames() throws SourcesScannerException {
        try {
            Path directoryPath = Paths.get(directory);
            JarFile jarFile = new JarFile(codeSourceFile);

            Set<String> set = new HashSet<>();

            for (JarEntry jarEntry : Collections.list(jarFile.entries())) {
                String jarEntryName = jarEntry.getName();
                Path jarEntryPath = Paths.get(jarEntryName);

                if ((!directory.isEmpty() && !jarEntryPath.startsWith(directoryPath)) || !jarEntryName.endsWith(".class")) {
                    continue;
                }

                set.add(jarEntryPath.toString());
            }

            return set;
        } catch (IOException e) {
            throw new SourcesScannerException("Cannot open .jar file: " + e.getMessage(), e);
        }
    }
}