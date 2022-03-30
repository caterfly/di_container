package di.sourcesscanner;

import java.io.File;
import java.net.URISyntaxException;

public class Utils {
    public static File getCodeSourceFile(Class<?> clazz) throws SourcesScannerException {
        try {
            return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new SourcesScannerException("Cannot convert location of the " + clazz.getName() + "to URI", e);
        }
    }
}