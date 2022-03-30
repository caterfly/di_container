package di.container.dependency;

import di.container.BeanFactory;
import di.container.DIContainerException;

public class DependencyWithType implements Dependency {

    private final BeanFactory beanFactory;
    private String fieldName;
    private Class<?> clazz;

    public DependencyWithType(BeanFactory beanFactory, String fieldName, Class<?> clazz) {
        this.beanFactory = beanFactory;
        this.fieldName = fieldName;
        this.clazz = clazz;
    }

    public DependencyWithType(BeanFactory beanFactory, Class<?> clazz) {
        this.beanFactory = beanFactory;
        this.clazz = clazz;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Object getBean() throws DIContainerException {
        return beanFactory.getBean(clazz);
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }


}