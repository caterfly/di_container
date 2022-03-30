package di.container.dependency;

import di.container.BeanFactory;
import di.container.DIContainerException;

public class DependencyWithId implements Dependency {

    private final BeanFactory beanFactory;
    private final String id;
    private String fieldName;

    public DependencyWithId(BeanFactory beanFactory, String id) {
        this.beanFactory = beanFactory;
        this.id = id;
    }

    public DependencyWithId(BeanFactory beanFactory, String id, String fieldName) {
        this.beanFactory = beanFactory;
        this.id = id;
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Object getBean() throws DIContainerException {
        return beanFactory.getBean(id);
    }

    @Override
    public Class<?> getClazz() {
        return beanFactory.getBeanDescription(id).getClazz();
    }
}