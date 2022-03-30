package di.beanparser.objects;
public class Argument {
    private String ref;
    private String fieldName;
    private String className;
    private String value;
    private Bean bean;
    private boolean provider = false;

    @Override
    public String toString() {
        return "Argument{" +
                "ref='" + ref + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", className='" + className + '\'' +
                ", value='" + value + '\'' +
                ", bean=" + bean +
                ", provider=" + provider +
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

    public boolean isProvider() {
        return provider;
    }
}