package di.sourcesscanner;

import java.util.Set;

public interface SourcesScanner {
    Set<Class<?>> scan() throws SourcesScannerException;
}