package di.sourcesscanner;

public class SourcesScannerException extends Exception {
    public SourcesScannerException() {
        super();
    }

    public SourcesScannerException(String message) {
        super(message);
    }

    public SourcesScannerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourcesScannerException(Throwable cause) {
        super(cause);
    }
}