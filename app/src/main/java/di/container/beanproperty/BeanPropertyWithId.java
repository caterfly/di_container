package di.container.beanproperty;

import di.container.BeanFactory;
import di.container.DIContainerException;
import di.container.beanproperty.BeanProperty;

public class BeanPropertyWithId implements BeanProperty {
    private BeanFactory parentFactory;

    private String id;

    private Class<?> clazz;

    private String fieldName;

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Object getBean() throws DIContainerException {
        return parentFactory.getBean(id);
    }

    @Override
    public Class<?> getClazz() {
        return parentFactory.getBeanDescription(id).getClazz();
    }

    public BeanPropertyWithId(BeanFactory parentFactory, String id) {
        this.parentFactory = parentFactory;
        this.id = id;
    }

    public BeanPropertyWithId(BeanFactory parentFactory, String id, String fieldName) {
        this.parentFactory = parentFactory;
        this.id = id;
        this.fieldName = fieldName;
    }
}