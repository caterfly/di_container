package di.sourcesscanner;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSourcesScanner implements SourcesScanner {

    @Override
    public Set<Class<?>> scan() throws SourcesScannerException {
        Set<Class<?>> classes = new HashSet<>();

        for (String classFileName : getClassFileNames()) {
            classFileName = classFileName
                    .replaceAll(".class$", "")
                    .replaceAll("\\\\", ".");

            try {
                classes.add(Class.forName(classFileName));
            } catch (ClassNotFoundException e) {
                throw new SourcesScannerException(classFileName + "not found");
            }
        }

        return classes;
    }

    protected abstract Set<String> getClassFileNames() throws SourcesScannerException;
}