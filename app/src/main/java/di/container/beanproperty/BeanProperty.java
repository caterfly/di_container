package di.container.beanproperty;

import di.container.DIContainerException;

public interface BeanProperty {
    String getFieldName();
    Object getBean() throws DIContainerException;
    Class<?> getClazz();
}