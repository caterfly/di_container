package di.container.beanproperty;

import di.container.BeanDescription;
import di.container.DIContainerException;

public class InnerBeanProperty implements BeanProperty {
    private String fieldName;

    private BeanDescription description;

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Object getBean() throws DIContainerException {
        return description.getBean();
    }

    @Override
    public Class<?> getClazz() {
        return description.getClazz();
    }

    public InnerBeanProperty(String fieldName, BeanDescription description) {
        this.fieldName = fieldName;
        this.description = description;
    }

    public InnerBeanProperty(BeanDescription description) {
        this.description = description;
    }
}