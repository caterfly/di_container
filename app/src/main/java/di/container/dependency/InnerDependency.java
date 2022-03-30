package di.container.dependency;

import di.container.BeanDescription;
import di.container.DIContainerException;

public class InnerDependency implements Dependency {
    private String fieldName;

    private final BeanDescription beanDescription;

    public InnerDependency(String fieldName, BeanDescription beanDescription) {
        this.fieldName = fieldName;
        this.beanDescription = beanDescription;
    }

    public InnerDependency(BeanDescription beanDescription) {
        this.beanDescription = beanDescription;
    }

    private BeanDescription description;

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Object getBean() throws DIContainerException {
        return beanDescription.getBean();
    }

    @Override
    public Class<?> getClazz() {
        return beanDescription.getClazz();
    }

}