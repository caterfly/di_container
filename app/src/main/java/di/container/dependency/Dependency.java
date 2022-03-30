package di.container.dependency;

import di.container.DIContainerException;

public interface Dependency {

    String getFieldName();

    Object getBean() throws DIContainerException;

    Class<?> getClazz();
}