package di.beanparser;

public class BeanParserException extends Exception {
    public BeanParserException() {
        super();
    }

    public BeanParserException(String message) {
        super(message);
    }

    public BeanParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanParserException(Throwable cause) {
        super(cause);
    }
}