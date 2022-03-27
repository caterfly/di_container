package di.jsonparser.objects;

public class Argument {

    private String ref;
    private String fieldName;
    private String className;
    private String value;
    private Bean bean;

    @Override
    public String toString() {
        return "Argument{" +
                "ref='" + ref + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", type='" + className + '\'' +
                ", value='" + value + '\'' +
                ", bean=" + bean +
                '}';
    }

    public String getRef() {
        return ref;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getClassName() {
        return className;
    }

    public String getValue() {
        return value;
    }

    public Bean getBean() {
        return bean;
    }
}