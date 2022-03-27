package di.container.beanproperty;

public class BeanPropertyWithValue implements BeanProperty {

    private Object value;

    private Class<?> clazz;

    private String fieldName;

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Object getBean() {
        return value;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }

    public BeanPropertyWithValue(String fieldName, Object value, Class<?> clazz) {
        this.fieldName = fieldName;
        this.value = value;
        this.clazz = clazz;
    }

    public BeanPropertyWithValue(Object value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }
}