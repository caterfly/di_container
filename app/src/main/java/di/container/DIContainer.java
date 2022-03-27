package di.container;

import java.util.Collection;

public interface DIContainer {
    <T> T getBean(String name, Class<T> clazz) throws DIContainerException;

    Object getBean(String name) throws DIContainerException;

    Collection<String> getBeans();
}