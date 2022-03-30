package di.sourcesscanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectorySourcesScanner extends AbstractSourcesScanner {

    private final String directory;
    private final List<Class<?>> startupClasses;

    public DirectorySourcesScanner(String directory, Class<?>... startupClasses) {
        this.directory = directory;
        this.startupClasses = new ArrayList<>();
        this.startupClasses.add(getClass());
        this.startupClasses.addAll(Arrays.asList(startupClasses));
    }

    @Override
    protected Set<String> getClassFileNames() throws SourcesScannerException {
        Set<String> set = new HashSet<>();

        for (Class<?> clazz : startupClasses) {
            set.addAll(getClassFileNames(directory, Utils.getCodeSourceFile(clazz)));
        }

        return set;
    }

    private Set<String> getClassFileNames(String directory, File codeSourceFile) throws SourcesScannerException {
        Path directoryPath = Paths.get(directory);
        Path codeSourcePath = codeSourceFile.toPath();
        try (Stream<Path> stream = Files.walk(codeSourcePath)) {
            return stream
                    .filter(path -> !Files.isDirectory(path) && path.toString().endsWith(".class"))
                    .map(codeSourcePath::relativize)
                    .filter(path -> directory.isEmpty() || path.startsWith(directoryPath))
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new SourcesScannerException("Cannot walk the directory " + codeSourcePath, e);
        }
    }
}